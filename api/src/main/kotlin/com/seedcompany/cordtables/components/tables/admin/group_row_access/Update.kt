package com.seedcompany.cordtables.components.tables.admin.group_row_access

import com.seedcompany.cordtables.common.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupRowAccessUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminGroupRowAccessUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupRowAccessUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/group-row-access/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminGroupRowAccessUpdateRequest): AdminGroupRowAccessUpdateResponse {

        if (req.token == null) return AdminGroupRowAccessUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return AdminGroupRowAccessUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return AdminGroupRowAccessUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "group_id" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.group_row_access",
                    column = "group_id",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "table_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.group_row_access",
                    column = "table_name",
                    id = req.id,
                    value = req.value,
                    cast = "::admin.table_name"
                )
            }
            "row" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.group_row_access",
                    column = "row",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.group_row_access",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.group_row_access",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return AdminGroupRowAccessUpdateResponse(ErrorType.NoError)
    }

}
