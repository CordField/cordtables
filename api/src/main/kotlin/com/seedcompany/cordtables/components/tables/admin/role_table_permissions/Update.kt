package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.AdminRoleTablePermissionsUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.Update as CommonUpdate
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.AdminRoleTablePermissionsUpdateResponse
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.roleTablePermissionInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleTablePermissionsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminRoleTablePermissionsUpdateResponse(
    val error: ErrorType,
)



@Controller("AdminRoleTablePermissionsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin-role-table-permissions/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminRoleTablePermissionsUpdateRequest): AdminRoleTablePermissionsUpdateResponse {

        if (req.token == null) return AdminRoleTablePermissionsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return AdminRoleTablePermissionsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return AdminRoleTablePermissionsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "role" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_table_permissions",
                    column = "role",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "table_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_table_permissions",
                    column = "table_name",
                    id = req.id,
                    value = req.value,
                    cast = "::admin.table_name"
                )
            }
            "table_permission" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_table_permissions",
                    column = "table_permission",
                    id = req.id,
                    value = req.value,
                    cast = "::admin.table_permission_grant_type"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_table_permissions",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_table_permissions",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return AdminRoleTablePermissionsUpdateResponse(ErrorType.NoError)
    }

}