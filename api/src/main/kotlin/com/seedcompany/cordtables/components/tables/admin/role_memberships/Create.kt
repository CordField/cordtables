package com.seedcompany.cordtables.components.tables.admin.role_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_memberships.roleMembershipInput
import com.seedcompany.cordtables.components.tables.admin.role_memberships.Read
import com.seedcompany.cordtables.components.tables.admin.role_memberships.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleMembershipsCreateRequest(
    val token: String? = null,
    val roleMembership: roleMembershipInput,
)

data class AdminRoleMembershipsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleMembershipsCreate")
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

    @PostMapping("admin-role-memberships/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminRoleMembershipsCreateRequest): AdminRoleMembershipsCreateResponse {

        // if (req.roleMembership.name == null) return AdminRoleMembershipsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into admin.role_memberships(role, person,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
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
            req.roleMembership.role,
            req.roleMembership.person,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return AdminRoleMembershipsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
