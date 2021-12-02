package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.*
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.AdminRoleTablePermissionsCreateRequest
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleTablePermissionsCreateReadRequest(
    val token: String? = null,
    val roleTablePermission: roleTablePermissionInput,
)

data class AdminRoleTablePermissionsCreateReadResponse(
    val error: ErrorType,
    val roleTablePermission: roleTablePermission? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleTablePermissionsCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("admin-role-table-permissions/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: AdminRoleTablePermissionsCreateReadRequest): AdminRoleTablePermissionsCreateReadResponse {

        val createResponse = create.createHandler(
            AdminRoleTablePermissionsCreateRequest(
                token = req.token,
                roleTablePermission = req.roleTablePermission
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return AdminRoleTablePermissionsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            AdminRoleTablePermissionsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return AdminRoleTablePermissionsCreateReadResponse(error = readResponse.error, roleTablePermission = readResponse.roleTablePermission)
    }
}