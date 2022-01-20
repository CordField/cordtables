package com.seedcompany.cordtables.components.tables.admin.role_column_grants

import com.seedcompany.cordtables.common.AccessLevels
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.TableNames
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleUpdateReadResponse
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.roleColumnGrantInput
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.Read
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleColumnGrantsCreateRequest(
    val token: String? = null,
    val roleColumnGrant: roleColumnGrantInput,
)

data class AdminRoleColumnGrantsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleColumnGrantsCreate")
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

    @PostMapping("admin/role-column-grants/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminRoleColumnGrantsCreateRequest): AdminRoleColumnGrantsCreateResponse {

      if (req.token == null) return AdminRoleColumnGrantsCreateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminRoleColumnGrantsCreateResponse(ErrorType.AdminOnly)

        // if (req.roleColumnGrant.name == null) return AdminRoleColumnGrantsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into admin.role_column_grants(role, table_name, column_name, access_level,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::admin.table_name,
                    ?,
                    ?::admin.access_level,
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
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.roleColumnGrant.role,
            req.roleColumnGrant.table_name,
            req.roleColumnGrant.column_name,
            req.roleColumnGrant.access_level,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

        return AdminRoleColumnGrantsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
