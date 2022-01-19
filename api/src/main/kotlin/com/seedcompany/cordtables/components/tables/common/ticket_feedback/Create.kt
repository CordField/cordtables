package com.seedcompany.cordtables.components.tables.common.ticket_feedback

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


data class CommonTicketFeedbackCreateRequest(
    val token: String? = null,
    val ticket_feedback: CommonTicketFeedbackInput
)

data class CommonTicketFeedbackCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketFeedbackCreate")

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

    @PostMapping("common/ticket-feedback/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonTicketFeedbackCreateRequest): CommonTicketFeedbackCreateResponse {

        if (req.token == null) return CommonTicketFeedbackCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.ticket_feedback(ticket, stakeholder, feedback, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?::common.ticket_feedback_options,
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
            req.ticket_feedback.ticket,
            req.ticket_feedback.stakeholder,
            req.ticket_feedback.feedback,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

        return CommonTicketFeedbackCreateResponse(error = ErrorType.NoError, id = id)
    }


}
