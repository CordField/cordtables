package com.seedcompany.cordtables.components.tables.common.org_chart_position_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.orgChartPositionGraphInput
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.Read
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrgChartPositionGraphCreateRequest(
    val token: String? = null,
    val orgChartPositionGraph: orgChartPositionGraphInput,
)

data class CommonOrgChartPositionGraphCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrgChartPositionGraphCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common/org-chart-position-graph/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonOrgChartPositionGraphCreateRequest): CommonOrgChartPositionGraphCreateResponse {

        // if (req.orgChartPositionGraph.name == null) return CommonOrgChartPositionGraphCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.org_chart_position_graph(from_position, to_position, relationship_type, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?::common.position_relationship_types,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.orgChartPositionGraph.from_position,
            req.orgChartPositionGraph.to_position,
            req.orgChartPositionGraph.relationship_type,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

//        req.language.id = id

        return CommonOrgChartPositionGraphCreateResponse(error = ErrorType.NoError, id = id)
    }

}
