package com.seedcompany.cordtables.components.tables.common.ticket_assignments

import com.seedcompany.cordtables.components.tables.common.ticket_assignments.*
import com.seedcompany.cordtables.components.tables.common.ticket_assignments.Create
import com.seedcompany.cordtables.components.tables.common.ticket_assignments.Read


import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonTicketAssignmentCreateReadRequest(
        val token: String? = null,
        val ticket_assignment: CommonTicketAssignmentsInput,
)

data class CommonTicketAssignmentCreateReadResponse(
        val error: ErrorType,
            val ticket_assignment: CommonTicketAssignments? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketAssignmentsCreateRead")
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
    @PostMapping("common-ticket-assignments/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonTicketAssignmentCreateReadRequest): CommonTicketAssignmentCreateReadResponse {

        val createResponse = create.createHandler(
                CommonTicketAssignmentCreateRequest(
                        token = req.token,
                        ticket_assignment = req.ticket_assignment
                )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonTicketAssignmentCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
                CommonTicketAssignmentReadRequest(
                        token = req.token,
                        id = createResponse!!.id
                )
        )

        return CommonTicketAssignmentCreateReadResponse(error = readResponse.error, ticket_assignment = readResponse.ticket_assignment)
    }
}
