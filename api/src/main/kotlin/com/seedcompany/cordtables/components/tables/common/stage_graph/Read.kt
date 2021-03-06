package com.seedcompany.cordtables.components.tables.common.stage_graph

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

data class CommonStageGraphReadRequest(
    val token: String?,
    val id: String? = null,
)

data class CommonStageGraphReadResponse(
    val error: ErrorType,
    val stageGraph: stageGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageGraphRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/stage-graph/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonStageGraphReadRequest): CommonStageGraphReadResponse {

        if (req.token == null) return CommonStageGraphReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonStageGraphReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.stage_graph",
                getList = false,
                columns = arrayOf(
                    "id",
                    "from_stage",
                    "to_stage",
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

                var from_stage: String? = jdbcResult.getString("from_stage")
                if (jdbcResult.wasNull()) from_stage = null

                var to_stage: String? = jdbcResult.getString("to_stage")
                if (jdbcResult.wasNull()) to_stage = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val stageGraph =
                    stageGraph(
                        id = id,
                        from_stage = from_stage,
                        to_stage = to_stage,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return CommonStageGraphReadResponse(ErrorType.NoError, stageGraph = stageGraph)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonStageGraphReadResponse(ErrorType.SQLReadError)
        }

        return CommonStageGraphReadResponse(error = ErrorType.UnknownError)
    }
}
