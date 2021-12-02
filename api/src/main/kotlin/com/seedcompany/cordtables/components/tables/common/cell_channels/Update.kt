package com.seedcompany.cordtables.components.tables.common.cell_channels

import com.seedcompany.cordtables.common.CommonTicketStatus
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCellChannelsUpdateRequest(
        val token: String?,
        val id: Int? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonCellChannelsUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCellChannelsUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-cell-channels/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonCellChannelsUpdateRequest): CommonCellChannelsUpdateResponse {

        if (req.token == null) return CommonCellChannelsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonCellChannelsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonCellChannelsUpdateResponse(ErrorType.MissingId)
        println(req)

        when (req.column) {

            "table_name" -> {
                util.updateField(
                        token = req.token,
                        table = "common.cell_channels",
                        column = "table_name",
                        id = req.id,
                        value = req.value,
                        cast = "::admin.table_name"
                )
            }
            "column_name" -> {
                util.updateField(
                        token = req.token,
                        table = "common.cell_channels",
                        column = "column_name",
                        id = req.id,
                        value = req.value
                )
            }
            "row" -> {
                util.updateField(
                        token = req.token,
                        table = "common.cell_channels",
                        column = "row",
                        id = req.id,
                        value = req.value,
                        cast="::integer"
                )
            }
            "owning_person" -> {
                util.updateField(
                        token = req.token,
                        table = "common.cell_channels",
                        column = "owning_person",
                        id = req.id,
                        value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                        token = req.token,
                        table = "common.cell_channels",
                        column = "owning_group",
                        id = req.id,
                        value = req.value
                )
            }
        }

        return CommonCellChannelsUpdateResponse(ErrorType.NoError)
    }
}