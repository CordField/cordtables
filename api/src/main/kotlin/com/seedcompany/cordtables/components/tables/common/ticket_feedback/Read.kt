package com.seedcompany.cordtables.components.tables.common.ticket_feedback

import com.seedcompany.cordtables.common.CommonTicketFeedbackOptions
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonTicketFeedbackReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class CommonTicketFeedbackReadResponse(
    val error: ErrorType,
    val ticket_feedback: CommonTicketFeedback? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonTicketFeedbackRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-ticket-feedback/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonTicketFeedbackReadRequest): CommonTicketFeedbackReadResponse {

        if (req.token == null) return CommonTicketFeedbackReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonTicketFeedbackReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.ticket_feedback",
                getList = false,
                columns = arrayOf(
                    "id",
                    "ticket",
                    "stakeholder",
                    "feedback",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var ticket: Int? = jdbcResult.getInt("ticket")
                if (jdbcResult.wasNull()) ticket = null

                var stakeHolder: Int? = jdbcResult.getInt("stakeholder")
                if (jdbcResult.wasNull()) stakeHolder = null

                var feedback: String? = jdbcResult.getString("feedback")
                if (jdbcResult.wasNull()) feedback = null

                var createdAt: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) createdAt = null

                var createdBy: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) createdBy = null

                var modifiedAt: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modifiedAt = null

                var modifiedBy: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modifiedBy = null

                var owningPerson: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owningPerson = null

                var owningGroup: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owningGroup = null

                val ticket_feedback =
                    CommonTicketFeedback(
                        id = id,
                        ticket = ticket,
                        stake_holder = stakeHolder,
                        feedback = if(feedback == null) null else CommonTicketFeedbackOptions.valueOf(feedback),
                        created_at = createdAt,
                        created_by = createdBy,
                        modified_at = modifiedAt,
                        modified_by = modifiedBy,
                        owning_person = owningPerson,
                        owning_group = owningGroup
                    )

                return CommonTicketFeedbackReadResponse(ErrorType.NoError, ticket_feedback = ticket_feedback)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonTicketFeedbackReadResponse(ErrorType.SQLReadError)
        }

        return CommonTicketFeedbackReadResponse(error = ErrorType.UnknownError)
    }
}
