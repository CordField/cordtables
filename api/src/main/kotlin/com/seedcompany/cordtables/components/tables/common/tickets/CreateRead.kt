package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.tickets.*
import com.seedcompany.cordtables.components.tables.common.tickets.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonTicketsCreateReadRequest(
        val token: String? = null,
        val ticket: CommonTicketsInput,
)

data class CommonTicketsCreateReadResponse(
        val error: ErrorType,
        val ticket: CommonTickets? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonTicketsCreateRead")
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
    @PostMapping("common-tickets/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonTicketsCreateReadRequest): CommonTicketsCreateReadResponse {

        val createResponse = create.createHandler(
                CommonTicketsCreateRequest(
                        token = req.token,
                        ticket = req.ticket
                )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonTicketsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
                CommonTicketsReadRequest(
                        token = req.token,
                        id = createResponse!!.id
                )
        )

        return CommonTicketsCreateReadResponse(error = readResponse.error, ticket = readResponse.ticket)
    }
}