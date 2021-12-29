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

data class CommonTicketAssignmentUpdateReadRequest(
        val token: String?,
        val id: Int? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonTicketAssignmentUpdateReadResponse(
        val error: ErrorType,
        val ticket_assignment: CommonTicketAssignments? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketAssignmentsUpdateRead")
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
    @PostMapping("common/ticket-assignments/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonTicketAssignmentUpdateReadRequest): CommonTicketAssignmentUpdateReadResponse {

        val updateResponse = update.updateHandler(
                CommonTicketAssignmentUpdateRequest(
                        token = req.token,
                        column = req.column,
                        id = req.id,
                        value = req.value,
                )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonTicketAssignmentUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
                CommonTicketAssignmentReadRequest(
                        token = req.token,
                        id = req.id!!
                )
        )

        return CommonTicketAssignmentUpdateReadResponse(error = readResponse.error, readResponse.ticket_assignment)
    }
}
