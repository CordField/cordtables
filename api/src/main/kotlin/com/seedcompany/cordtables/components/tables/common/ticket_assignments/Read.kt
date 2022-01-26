package com.seedcompany.cordtables.components.tables.common.ticket_assignments

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

data class CommonTicketAssignmentReadRequest(
        val token: String?,
        val ticket: String?,
        val id: String? = null,
)

data class CommonTicketAssignmentReadResponse(
        val error: ErrorType,
        val ticket_assignment: MutableList<CommonTicketAssignments>? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketAssignmentsRead")
class Read(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/ticket-assignments/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonTicketAssignmentReadRequest): CommonTicketAssignmentReadResponse {
        var data: MutableList<CommonTicketAssignments> = mutableListOf()
        if (req.token == null) return CommonTicketAssignmentReadResponse(ErrorType.TokenNotFound)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = "select id, ticket, person from common.ticket_assignments where ticket = '${req.ticket}'"

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var ticketId: String? = jdbcResult.getString("ticket")
                if (jdbcResult.wasNull()) ticketId = null

                var personId: String? = jdbcResult.getString("person")
                if (jdbcResult.wasNull()) personId = null


                data.add(
                        CommonTicketAssignments(
                                id = id,
                                ticket = ticketId,
                                person = personId,
                        )
                )


            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonTicketAssignmentReadResponse(ErrorType.SQLReadError)
        }

        return CommonTicketAssignmentReadResponse(ErrorType.NoError, data)
    }
}
