package com.seedcompany.cordtables.components.tables.common.ticket_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonTicketGraphUpdateReadRequest(
        val token: String?,
        val id: Int? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonTicketGraphUpdateReadResponse(
        val error: ErrorType,
        val ticket_graph: CommonTicketGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonTicketGraphUpdateRead")
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
    @PostMapping("common-ticket-graph/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonTicketGraphUpdateReadRequest): CommonTicketGraphUpdateReadResponse {

        val updateResponse = update.updateHandler(
                CommonTicketGraphUpdateRequest(
                        token = req.token,
                        column = req.column,
                        id = req.id,
                        value = req.value,
                )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonTicketGraphUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
                CommonTicketGraphReadRequest(
                        token = req.token,
                        id = req.id!!
                )
        )

        return CommonTicketGraphUpdateReadResponse(error = readResponse.error, readResponse.ticket_graph)
    }
}
