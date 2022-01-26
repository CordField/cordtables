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
        val value: Any? = null,
        val ticket: String? = null,
        val person: String? = null,

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
        if (req.id == null) return CommonTicketAssignmentUpdateResponse(ErrorType.MissingId)

        println(req)

                util.updateField(
                        token = req.token,
                        table = "common.ticket_assignments",
                        column = "ticket",
                        id = req.id,
                        value = req.ticket
                )


                util.updateField(
                        token = req.token,
                        table = "common.ticket_assignments",
                        column = "person",
                        id = req.id,
                        value = req.person
                )




        return CommonTicketAssignmentUpdateResponse(ErrorType.NoError)
    }
}
