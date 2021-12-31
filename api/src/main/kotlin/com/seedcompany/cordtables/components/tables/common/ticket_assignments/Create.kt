package com.seedcompany.cordtables.components.tables.common.ticket_assignments

import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.languages.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource


data class CommonTicketAssignmentCreateRequest(
        val token: String? = null,
        val ticket_assignment: CommonTicketAssignmentsInput
)

data class CommonTicketAssignmentCreateResponse(
        val error: ErrorType,
        val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketAssignmentsCreate")

class Create(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val update: Update,

        @Autowired
        val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common/ticket-assignments/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonTicketAssignmentCreateRequest): CommonTicketAssignmentCreateResponse {

        if (req.token == null) return CommonTicketAssignmentCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
                """
            insert into common.ticket_assignments(ticket, person, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
                String::class.java,
                req.ticket_assignment.ticket,
                req.ticket_assignment.person,
                req.token,
                req.token,
                req.token,
                util.adminGroupId
        )

        return CommonTicketAssignmentCreateResponse(error = ErrorType.NoError, id = id)
    }


}

