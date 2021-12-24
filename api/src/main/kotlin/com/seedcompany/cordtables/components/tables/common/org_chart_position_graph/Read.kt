package com.seedcompany.cordtables.components.tables.common.org_chart_position_graph

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.orgChartPositionGraph
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocation
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

data class CommonOrgChartPositionGraphReadRequest(
    val token: String?,
    val id: String? = null,
)

data class CommonOrgChartPositionGraphReadResponse(
    val error: ErrorType,
    val orgChartPositionGraph: orgChartPositionGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrgChartPositionGraphRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/org-chart-position-graph/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonOrgChartPositionGraphReadRequest): CommonOrgChartPositionGraphReadResponse {

        if (req.token == null) return CommonOrgChartPositionGraphReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonOrgChartPositionGraphReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.org_chart_position_graph",
                getList = false,
                columns = arrayOf(
                    "id",
                    "from_position",
                    "to_position",
                    "relationship_type",
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

                var from_position: String? = jdbcResult.getString("from_position")
                if (jdbcResult.wasNull()) from_position = null

                var to_position: String? = jdbcResult.getString("to_position")
                if (jdbcResult.wasNull()) to_position = null

                var relationship_type: String? = jdbcResult.getString("relationship_type")
                if (jdbcResult.wasNull()) relationship_type = null

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

                val orgChartPositionGraph =
                    orgChartPositionGraph(
                        id = id,
                        from_position = from_position,
                        to_position = to_position,
                        relationship_type = relationship_type,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return CommonOrgChartPositionGraphReadResponse(ErrorType.NoError, orgChartPositionGraph = orgChartPositionGraph)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonOrgChartPositionGraphReadResponse(ErrorType.SQLReadError)
        }

        return CommonOrgChartPositionGraphReadResponse(error = ErrorType.UnknownError)
    }
}
