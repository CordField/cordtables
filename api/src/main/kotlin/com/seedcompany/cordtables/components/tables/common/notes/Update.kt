package com.seedcompany.cordtables.components.tables.common.notes

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonNotesUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonNotesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonNotesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/notes/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonNotesUpdateRequest): CommonNotesUpdateResponse {

        if (req.token == null) return CommonNotesUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonNotesUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonNotesUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "table_name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.notes",
                    column = "table_name",
                    id = req.id,
                    value = req.value,
                    cast = "::admin.table_name"
                )
            }
            "column_name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.notes",
                    column = "column_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "row" -> {
                util.updateField(
                    token = req.token,
                    table = "common.notes",
                    column = "row",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "content" -> {
                util.updateField(
                    token = req.token,
                    table = "common.notes",
                    column = "content",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.notes",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.notes",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return CommonNotesUpdateResponse(ErrorType.NoError)
    }

}
