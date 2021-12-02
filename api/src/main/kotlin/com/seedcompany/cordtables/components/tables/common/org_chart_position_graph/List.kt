package com.seedcompany.cordtables.components.tables.common.org_chart_position_graph

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.orgChartPositionGraph
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


data class CommonOrgChartPositionGraphListRequest(
    val token: String?
)

data class CommonOrgChartPositionGraphListResponse(
    val error: ErrorType,
    val orgChartPositionGraphs: MutableList<orgChartPositionGraph>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrgChartPositionGraphList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-org-chart-position-graph/list")
    @ResponseBody
    fun listHandler(@RequestBody req:CommonOrgChartPositionGraphListRequest): CommonOrgChartPositionGraphListResponse {
        var data: MutableList<orgChartPositionGraph> = mutableListOf()
        if (req.token == null) return CommonOrgChartPositionGraphListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.org_chart_position_graph",
                filter = "order by id",
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
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var from_position: Int? = jdbcResult.getInt("from_position")
                if (jdbcResult.wasNull()) from_position = null

                var to_position: Int? = jdbcResult.getInt("to_position")
                if (jdbcResult.wasNull()) to_position = null

                var relationship_type: String? = jdbcResult.getString("relationship_type")
                if (jdbcResult.wasNull()) relationship_type = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
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
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonOrgChartPositionGraphListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return CommonOrgChartPositionGraphListResponse(ErrorType.NoError, data)
    }
}
