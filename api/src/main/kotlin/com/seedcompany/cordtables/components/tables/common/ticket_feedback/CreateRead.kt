package com.seedcompany.cordtables.components.tables.common.ticket_feedback

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonTicketFeedbackCreateReadRequest(
    val token: String? = null,
    val ticket_feedback: CommonTicketFeedbackInput,
)

data class CommonTicketFeedbackCreateReadResponse(
    val error: ErrorType,
    val ticket_feedback: CommonTicketFeedback? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonTicketFeedbackCreateRead")
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
    @PostMapping("common-ticket-feedback/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonTicketFeedbackCreateReadRequest): CommonTicketFeedbackCreateReadResponse {

        val createResponse = create.createHandler(
            CommonTicketFeedbackCreateRequest(
                token = req.token,
                ticket_feedback = req.ticket_feedback
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonTicketFeedbackCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonTicketFeedbackReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonTicketFeedbackCreateReadResponse(error = readResponse.error, ticket_feedback = readResponse.ticket_feedback)
    }
}