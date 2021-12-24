package com.seedcompany.cordtables.components.tables.common.ticket_assignments


import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonTicketAssignmentUpdateRequest(
        val token: String?,
        val id: String? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonTicketAssignmentUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketAssignmentsUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common/ticket-assignments/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonTicketAssignmentUpdateRequest): CommonTicketAssignmentUpdateResponse {

        if (req.token == null) return CommonTicketAssignmentUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonTicketAssignmentUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonTicketAssignmentUpdateResponse(ErrorType.MissingId)

        println(req)

        when (req.column) {

            "ticket" -> {
                util.updateField(
                        token = req.token,
                        table = "common.ticket_assignments",
                        column = "ticket",
                        id = req.id,
                        value = req.value
                )
            }

            "person" -> {
                util.updateField(
                        token = req.token,
                        table = "common.ticket_assignments",
                        column = "person",
                        id = req.id,
                        value = req.value
                )
            }


            "owning_person" -> {
                util.updateField(
                        token = req.token,
                        table = "common.ticket_assignments",
                        column = "owning_person",
                        id = req.id,
                        value = req.value,
                )
            }

            "owning_group" -> {
                util.updateField(
                        token = req.token,
                        table = "common.ticket_assignments",
                        column = "owning_group",
                        id = req.id,
                        value = req.value,
                )
            }
        }

        return CommonTicketAssignmentUpdateResponse(ErrorType.NoError)
    }
}
