package com.seedcompany.cordtables.components.tables.common.ticket_graph

import com.seedcompany.cordtables.components.tables.common.tickets.CommonTickets

import com.seedcompany.cordtables.common.CommonTicketStatus
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

data class CommonTicketGraphReadRequest(
        val token: String?,
        val id: String? = null,
)

data class CommonTicketGraphReadResponse(
        val error: ErrorType,
        val ticket_graph: CommonTicketGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketGraphRead")
class Read(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/ticket-graph/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonTicketGraphReadRequest): CommonTicketGraphReadResponse {

        if (req.token == null) return CommonTicketGraphReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonTicketGraphReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
                GetSecureListQueryRequest(
                        tableName = "common.ticket_graph",
                        getList = false,
                        columns = arrayOf(
                                "id",
                                "from_ticket",
                                "to_ticket",
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

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var fromTicket: Int? = jdbcResult.getInt("from_ticket")
                if (jdbcResult.wasNull()) fromTicket = null

                var toTicket: Int? = jdbcResult.getInt("to_ticket")
                if (jdbcResult.wasNull()) toTicket = null

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

                val ticket =
                        CommonTicketGraph(
                                id = id,
                                from_ticket = fromTicket,
                                to_ticket = toTicket,
                                created_at = createdAt,
                                created_by = createdBy,
                                modified_at = modifiedAt,
                                modified_by = modifiedBy,
                                owning_person = owningPerson,
                                owning_group = owningGroup
                        )

                return CommonTicketGraphReadResponse(ErrorType.NoError, ticket_graph = ticket)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonTicketGraphReadResponse(ErrorType.SQLReadError)
        }

        return CommonTicketGraphReadResponse(error = ErrorType.UnknownError)
    }
}
