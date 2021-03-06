package com.seedcompany.cordtables.components.tables.common.tickets

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

data class CommonTicketsReadRequest(
        val token: String?,
        val id: String? = null,
)

data class CommonTicketsReadResponse(
        val error: ErrorType,
        val ticket: CommonTickets? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketsRead")
class Read(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/tickets/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonTicketsReadRequest): CommonTicketsReadResponse {

        if (req.token == null) return CommonTicketsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonTicketsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
                GetSecureListQueryRequest(
                        tableName = "common.tickets",
                        getList = false,
                        columns = arrayOf(
                                "id",
                                "title",
                                "ticket_status",
                                "parent",
                                "content",
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

                var title: String? = jdbcResult.getString("title")
                if (jdbcResult.wasNull()) title = null

                var ticketStatus: String? = jdbcResult.getString("ticket_status")
                if (jdbcResult.wasNull()) ticketStatus = null

                var parent: String? = jdbcResult.getString("parent")
                if (jdbcResult.wasNull()) parent = null

                var content: String? = jdbcResult.getString("content")
                if (jdbcResult.wasNull()) content = null

                var createdAt: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) createdAt = null

                var createdBy: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) createdBy = null

                var modifiedAt: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modifiedAt = null

                var modifiedBy: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modifiedBy = null

                var owningPerson: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owningPerson = null

                var owningGroup: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owningGroup = null

                val ticket =
                        CommonTickets(
                                id = id,
                                title = title,
                                ticket_status = if(ticketStatus == null) null else CommonTicketStatus.valueOf(ticketStatus),
                                parent = parent,
                                content = content,
                                created_at = createdAt,
                                created_by = createdBy,
                                modified_at = modifiedAt,
                                modified_by = modifiedBy,
                                owning_person = owningPerson,
                                owning_group = owningGroup
                        )

                return CommonTicketsReadResponse(ErrorType.NoError, ticket = ticket)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonTicketsReadResponse(ErrorType.SQLReadError)
        }

        return CommonTicketsReadResponse(error = ErrorType.UnknownError)
    }
}
