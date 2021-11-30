package com.seedcompany.cordtables.components.tables.common.cell_channels


import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.partners.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCellChannelsCreateReadRequest(
        val token: String? = null,
        val cell_channel: CellChannelInput
)

data class CommonCellChannelsCreateReadResponse(
        val error: ErrorType,
        val cell_channel: CellChannel? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScCellChannelsCreateRead")
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
    @PostMapping("common-cell-channels/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonCellChannelsCreateReadRequest): CommonCellChannelsCreateReadResponse {

        val createResponse = create.createHandler(
                CommonCellChannelsCreateRequest(
                        token = req.token,
                        cell_channel = req.cell_channel
                )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonCellChannelsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
                CommonCellChannelsReadRequest(
                        token = req.token,
                        id = createResponse!!.id
                )
        )

        return CommonCellChannelsCreateReadResponse(error = readResponse.error, cell_channel = readResponse.cell_channel)
    }
}

