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

data class CommonTicketFeedbackUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonTicketFeedbackUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketFeedbackUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-ticket-feedback/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonTicketFeedbackUpdateRequest): CommonTicketFeedbackUpdateResponse {

        if (req.token == null) return CommonTicketFeedbackUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonTicketFeedbackUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonTicketFeedbackUpdateResponse(ErrorType.MissingId)

        println(req)

        when (req.column) {

            "ticket" -> {
                util.updateField(
                    token = req.token,
                    table = "common.ticket_feedback",
                    column = "ticket",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "stakeholder" -> {
                util.updateField(
                    token = req.token,
                    table = "common.ticket_feedback",
                    column = "stakeholder",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "feedback" -> {
                util.updateField(
                    token = req.token,
                    table = "common.ticket_feedback",
                    column = "feedback",
                    id = req.id,
                    value = req.value,
                    cast = "::common.ticket_feedback_options"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.ticket_feedback",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.ticket_feedback",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return CommonTicketFeedbackUpdateResponse(ErrorType.NoError)
    }
}
