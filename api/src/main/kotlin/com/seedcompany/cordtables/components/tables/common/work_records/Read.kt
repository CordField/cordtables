package com.seedcompany.cordtables.components.tables.common.work_records

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

data class CommonWorkRecordReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class CommonWorkRecordReadResponse(
    val error: ErrorType,
    val work_record: CommonWorkRecords? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkRecordsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/work-records/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonWorkRecordReadRequest): CommonWorkRecordReadResponse {

        if (req.token == null) return CommonWorkRecordReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonWorkRecordReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.work_records",
                getList = false,
                columns = arrayOf(
                    "id",
                    "person",
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

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var hours: Int? = jdbcResult.getInt("hours")
                if (jdbcResult.wasNull()) hours = null

                var minutes: Int? = jdbcResult.getInt("minutes")
                if (jdbcResult.wasNull()) minutes = null

                var totalTime: Number? = jdbcResult.getFloat("total_time")
                if (jdbcResult.wasNull()) totalTime = null

                var comment: String? = jdbcResult.getString("comment")
                if (jdbcResult.wasNull()) comment = null

                var personId: Int? = jdbcResult.getInt("person")
                if (jdbcResult.wasNull()) personId = null

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

                val work_record =
                    CommonWorkRecords(
                        id = id,
                        person = personId,
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

                return CommonWorkRecordReadResponse(ErrorType.NoError, work_record = work_record)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonWorkRecordReadResponse(ErrorType.SQLReadError)
        }

        return CommonWorkRecordReadResponse(error = ErrorType.UnknownError)
    }
}
