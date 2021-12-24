package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

import com.seedcompany.cordtables.common.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleTablePermissionsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminRoleTablePermissionsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleTablePermissionsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/role-table-permissions/update")
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
                    value = req.value
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
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_table_permissions",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return AdminRoleTablePermissionsUpdateResponse(ErrorType.NoError)
    }

}
