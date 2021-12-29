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

data class CommonTicketGraphCreateReadRequest(
        val token: String? = null,
        val ticket_graph: CommonTicketGraphInput,
)

data class CommonTicketGraphCreateReadResponse(
        val error: ErrorType,
        val ticket_graph: CommonTicketGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketGraphCreateRead")
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
    @PostMapping("common/ticket-graph/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonTicketGraphCreateReadRequest): CommonTicketGraphCreateReadResponse {

        val createResponse = create.createHandler(
                CommonTicketGraphCreateRequest(
                        token = req.token,
                        ticket_graph = req.ticket_graph
                )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonTicketGraphCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
                CommonTicketGraphReadRequest(
                        token = req.token,
                        id = createResponse!!.id
                )
        )

        return CommonTicketGraphCreateReadResponse(error = readResponse.error, ticket_graph = readResponse.ticket_graph)
    }
}
