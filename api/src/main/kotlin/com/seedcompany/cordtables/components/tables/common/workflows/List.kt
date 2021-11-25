/*package com.seedcompany.cordtables.components.tables.common.workflows

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


data class CommonWorkflowsListRequest(
        val token: String?
)

data class CommonWorkflowsListResponse(
        val error: ErrorType,
        val workflow: MutableList<CommonWorkflowsRecords>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonWorkRecordsList")
class List(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-work-records/list")
    @ResponseBody
    fun listHandler(@RequestBody req: CommonWorkflowsListRequest): CommonWorkflowsListResponse{
        var data: MutableList<CommonWorkflowsRecords> = mutableListOf()
        if (req.token == null) return CommonWorkflowsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
                GetSecureListQueryRequest(
                        tableName = "common.work_records",
                        filter = "order by id",
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
                        )
                )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var person: Int? = jdbcResult.getInt("person")
                if (jdbcResult.wasNull()) person = null

                var hours: Int? = jdbcResult.getInt("hours")
                if (jdbcResult.wasNull()) hours = null

                var minutes : Int? = jdbcResult.getInt("minutes")
                if (jdbcResult.wasNull()) minutes = null

                var totalTime : Number? = jdbcResult.getFloat("total_time")
                if (jdbcResult.wasNull()) totalTime = null

                var comment : String? = jdbcResult.getString("comment")
                if (jdbcResult.wasNull()) comment = null

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

                data.add(
                        CommonWorkflowsRecords(
                                id = id,
                                person = person,
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
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonWorkflowsListResponse(ErrorType.SQLReadError, mutableListOf())
        }
        return CommonWorkflowsListResponse(ErrorType.NoError, data)
    }
}
*/