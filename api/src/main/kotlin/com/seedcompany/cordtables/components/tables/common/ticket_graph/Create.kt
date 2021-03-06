package com.seedcompany.cordtables.components.tables.common.ticket_graph

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


data class CommonTicketGraphCreateRequest(
        val token: String? = null,
        val ticket_graph: CommonTicketGraphInput
)

data class CommonTicketGraphCreateResponse(
        val error: ErrorType,
        val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketGraphCreate")

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

    @PostMapping("common/ticket-graph/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonTicketGraphCreateRequest): CommonTicketGraphCreateResponse {

        if (req.token == null) return CommonTicketGraphCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
                """
            insert into common.ticket_graph(from_ticket, to_ticket, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
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
                    ?
                )
            returning id;
        """.trimIndent(),
                String::class.java,
                req.ticket_graph.from_ticket,
                req.ticket_graph.to_ticket,
                req.token,
                req.token,
                req.token,
                util.adminGroupId()
        )

        return CommonTicketGraphCreateResponse(error = ErrorType.NoError, id = id)
    }


}
