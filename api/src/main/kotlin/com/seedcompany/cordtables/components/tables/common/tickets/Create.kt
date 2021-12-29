package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.budget_records.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource


data class CommonTicketsCreateRequest(
        val token: String? = null,
        val ticket: CommonTicketsInput
)

data class CommonTicketsCreateResponse(
        val error: ErrorType,
        val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketsCreate")

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

    @PostMapping("common-tickets/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonTicketsCreateRequest): CommonTicketsCreateResponse {

        if (req.token == null) return CommonTicketsCreateResponse(error = ErrorType.InputMissingToken, null)

        // check enums and error out if needed
        if (!enumContains<CommonTicketStatus>(req.ticket.ticket_status)) {
            return CommonTicketsCreateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

    // create row with required fields, use id to update cells afterwards one by one
    val id = jdbcTemplate.queryForObject(
            """
            insert into common.tickets(ticket_status, parent, content, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::common.ticket_status,
                    ?::uuid,
                    ?,
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
            req.ticket.ticket_status,
            req.ticket.parent,
            req.ticket.content,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
    )

    return CommonTicketsCreateResponse(error = ErrorType.NoError, id = id)
}


}
