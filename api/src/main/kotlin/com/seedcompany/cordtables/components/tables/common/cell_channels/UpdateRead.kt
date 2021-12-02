package com.seedcompany.cordtables.components.tables.common.cell_channels

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.tickets.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCellChannelsUpdateReadRequest(
        val token: String?,
        val id: Int? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonCellChannelsUpdateReadResponse(
        val error: ErrorType,
        val cell_channel: CellChannel? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCellChannelsUpdateRead")
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
    @PostMapping("common-cell-channels/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonCellChannelsUpdateReadRequest): CommonCellChannelsUpdateReadResponse {

        val updateResponse = update.updateHandler(
                CommonCellChannelsUpdateRequest(
                        token = req.token,
                        column = req.column,
                        id = req.id,
                        value = req.value,
                )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonCellChannelsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
                CommonCellChannelsReadRequest(
                        token = req.token,
                        id = req.id!!
                )
        )

        return CommonCellChannelsUpdateReadResponse(error = readResponse.error, readResponse.cell_channel)
    }
}
