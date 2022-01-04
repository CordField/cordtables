package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonTicketsUpdateReadRequest(
        val token: String?,
        val id: String? = null,
        val column: String? = null,
        val value: Any? = null,
        val ticket: CommonTicketsInput
)

data class CommonTicketsUpdateReadResponse(
        val error: ErrorType,
        val ticket: CommonTickets? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketsUpdateRead")
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
    @PostMapping("common/tickets/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonTicketsUpdateReadRequest): CommonTicketsUpdateReadResponse {

        val updateResponse = update.updateHandler(
                CommonTicketsUpdateRequest(
                        token = req.token,
                        id = req.id,
                        ticket = req.ticket,
                )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonTicketsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
                CommonTicketsReadRequest(
                        token = req.token,
                        id = req.id!!
                )
        )

        return CommonTicketsUpdateReadResponse(error = readResponse.error, readResponse.ticket)
    }
}
