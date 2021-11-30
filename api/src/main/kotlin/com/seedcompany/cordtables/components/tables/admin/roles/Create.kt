package com.seedcompany.cordtables.components.tables.admin.roles

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.roles.roleInput
import com.seedcompany.cordtables.components.tables.admin.roles.Read
import com.seedcompany.cordtables.components.tables.admin.roles.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRolesCreateRequest(
    val token: String? = null,
    val role: roleInput,
)

data class AdminRolesCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)


@Controller("AdminRolesCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("admin-roles/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminRolesCreateRequest): AdminRolesCreateResponse {

        if (req.role.name == null) return AdminRolesCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into admin.roles(name, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.role.name,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return AdminRolesCreateResponse(error = ErrorType.NoError, id = id)
    }

}