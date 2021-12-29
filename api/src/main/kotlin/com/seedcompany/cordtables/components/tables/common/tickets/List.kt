package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.CommonTicketStatus
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.languages.*
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


data class CommonTicketsListRequest(
        val token: String?
)

data class CommonTicketsListResponse(
        val error: ErrorType,
        val tickets: MutableList<CommonTickets>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketsList")
class List(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-tickets/list")
    @ResponseBody
    fun listHandler(@RequestBody req: CommonTicketsListRequest): CommonTicketsListResponse{
        var data: MutableList<CommonTickets> = mutableListOf()
        if (req.token == null) return CommonTicketsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
                GetSecureListQueryRequest(
                        tableName = "common.tickets",
                        filter = "order by id",
                        columns = arrayOf(
                                "id",
                                "ticket_status",
                                "parent",
                                "content",
                                "created_at",
                                "created_by",
                                "modified_at",
                                "modified_by",
                                "owning_person",
                                "owning_group",
                        )
                )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var ticket_status: String? = jdbcResult.getString("ticket_status")
                if (jdbcResult.wasNull()) ticket_status = null

                var parent: Int? = jdbcResult.getInt("parent")
                if (jdbcResult.wasNull()) parent = null

                var content: String? = jdbcResult.getString("content")
                if(jdbcResult.wasNull()) content = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                        CommonTickets(
                                id = id,
                                ticket_status = if(ticket_status == null) null else CommonTicketStatus.valueOf(ticket_status),
                                parent = parent,
                                content = content,
                                created_at = created_at,
                                created_by = created_by,
                                modified_at = modified_at,
                                modified_by = modified_by,
                                owning_person = owning_person,
                                owning_group = owning_group
                        )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonTicketsListResponse(ErrorType.SQLReadError, mutableListOf())
        }
        return CommonTicketsListResponse(ErrorType.NoError, data)
    }
}