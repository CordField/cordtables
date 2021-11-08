package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.TableNames
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.roleTablePermissionInput
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.Read
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleTablePermissionsCreateRequest(
    val token: String? = null,
    val roleTablePermission: roleTablePermissionInput,
)

data class AdminRoleTablePermissionsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminRoleTablePermissionsCreate")
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

    @PostMapping("admin-role-table-permissions/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminRoleTablePermissionsCreateRequest): AdminRoleTablePermissionsCreateResponse {

        // if (req.roleTablePermission.name == null) return AdminRoleTablePermissionsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into admin.role_table_permissions(role, table_name, table_permission,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::admin.table_name,
                    ?::admin.table_permission_grant_type,
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
            req.roleTablePermission.role,
            req.roleTablePermission.table_name,
            req.roleTablePermission.table_permission,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return AdminRoleTablePermissionsCreateResponse(error = ErrorType.NoError, id = id)
    }

}