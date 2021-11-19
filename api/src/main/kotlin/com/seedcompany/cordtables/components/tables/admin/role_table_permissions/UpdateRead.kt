package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.AdminRoleTablePermissionsReadRequest
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.AdminRoleTablePermissionsUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.roleTablePermission
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.roleTablePermissionInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonFileVersionsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonFileVersionsUpdateReadResponse(
    val error: ErrorType,
    val roleTablePermission: roleTablePermission? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminRoleTablePermissionsUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("admin-role-table-permissions/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonFileVersionsUpdateReadRequest): CommonFileVersionsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            AdminRoleTablePermissionsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonFileVersionsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            AdminRoleTablePermissionsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonFileVersionsUpdateReadResponse(error = readResponse.error, readResponse.roleTablePermission)
    }
}