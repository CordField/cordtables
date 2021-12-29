package com.seedcompany.cordtables.components.tables.admin.role_column_grants

import com.seedcompany.cordtables.common.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleColumnGrantsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminRoleColumnGrantsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleColumnGrantsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/role-column-grants/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminRoleColumnGrantsUpdateRequest): AdminRoleColumnGrantsUpdateResponse {

        if (req.token == null) return AdminRoleColumnGrantsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return AdminRoleColumnGrantsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return AdminRoleColumnGrantsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "role" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_column_grants",
                    column = "role",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "table_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_column_grants",
                    column = "table_name",
                    id = req.id,
                    value = req.value,
                    cast = "::admin.table_name"
                )
            }
            "column_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_column_grants",
                    column = "column_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "access_level" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_column_grants",
                    column = "access_level",
                    id = req.id,
                    value = req.value,
                    cast = "::admin.access_level"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_column_grants",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_column_grants",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return AdminRoleColumnGrantsUpdateResponse(ErrorType.NoError)
    }

}
