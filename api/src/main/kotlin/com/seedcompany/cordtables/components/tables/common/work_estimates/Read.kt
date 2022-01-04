package com.seedcompany.cordtables.components.tables.common.work_estimates

import com.seedcompany.cordtables.components.tables.common.work_records.CommonWorkRecords

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

data class CommonWorkEstimateReadRequest(
    val token: String?,
    val id: String? = null,
    val ticket: String? = null,
)

data class CommonWorkEstimateReadResponse(
    val error: ErrorType,
    val work_estimate: CommonWorkEstimates? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkEstimatesRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/work-estimates/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonWorkEstimateReadRequest): CommonWorkEstimateReadResponse {

        if (req.token == null) return CommonWorkEstimateReadResponse(ErrorType.TokenNotFound)
        if (req.ticket == null) return CommonWorkEstimateReadResponse(ErrorType.MissingTicketId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("ticket", req.ticket)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.work_estimates",
                getList = false,
                columns = arrayOf(
                    "id",
                    "person",
                    "ticket",
                    "hours",
                    "minutes",
                    "total_time",
                    "comment",
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

                var personId: String? = jdbcResult.getString("person")
                if (jdbcResult.wasNull()) personId = null

                var ticket: String? = jdbcResult.getString("ticket")
                if (jdbcResult.wasNull()) ticket = null

                var hours: Int? = jdbcResult.getInt("hours")
                if (jdbcResult.wasNull()) hours = null

                var minutes: Int? = jdbcResult.getInt("minutes")
                if (jdbcResult.wasNull()) minutes = null

                var totalTime: Number? = jdbcResult.getFloat("total_time")
                if (jdbcResult.wasNull()) totalTime = null

                var comment: String? = jdbcResult.getString("comment")
                if (jdbcResult.wasNull()) comment = null

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

                val work_estimate =
                    CommonWorkEstimates(
                        id = id,
                        person = personId,
                        ticket = ticket,
                        hours = hours,
                        minutes = minutes,
                        total_time = totalTime,
                        comment = comment,
                        created_at = createdAt,
                        created_by = createdBy,
                        modified_at = modifiedAt,
                        modified_by = modifiedBy,
                        owning_person = owningPerson,
                        owning_group = owningGroup
                    )

                return CommonWorkEstimateReadResponse(ErrorType.NoError, work_estimate = work_estimate)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonWorkEstimateReadResponse(ErrorType.SQLReadError)
        }

        return CommonWorkEstimateReadResponse(error = ErrorType.UnknownError)
    }
}
