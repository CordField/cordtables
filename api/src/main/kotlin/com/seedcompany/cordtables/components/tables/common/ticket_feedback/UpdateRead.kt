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

data class CommonTicketFeedbackUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonTicketFeedbackUpdateReadResponse(
    val error: ErrorType,
    val ticket_feedback: CommonTicketFeedback? = null,
)


@Controller("CommonTicketFeedbackUpdateRead")
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
    @PostMapping("common-ticket-feedback/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonTicketFeedbackUpdateReadRequest): CommonTicketFeedbackUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonTicketFeedbackUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonTicketFeedbackUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonTicketFeedbackReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonTicketFeedbackUpdateReadResponse(error = readResponse.error, readResponse.ticket_feedback)
    }
}
