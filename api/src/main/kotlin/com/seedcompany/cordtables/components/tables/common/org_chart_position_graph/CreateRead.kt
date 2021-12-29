package com.seedcompany.cordtables.components.tables.common.org_chart_position_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.*
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.CommonOrgChartPositionGraphCreateRequest
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrgChartPositionGraphCreateReadRequest(
    val token: String? = null,
    val orgChartPositionGraph: orgChartPositionGraphInput,
)

data class CommonOrgChartPositionGraphCreateReadResponse(
    val error: ErrorType,
    val orgChartPositionGraph: orgChartPositionGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrgChartPositionGraphCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("common/org-chart-position-graph/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonOrgChartPositionGraphCreateReadRequest): CommonOrgChartPositionGraphCreateReadResponse {

        val createResponse = create.createHandler(
            CommonOrgChartPositionGraphCreateRequest(
                token = req.token,
                orgChartPositionGraph = req.orgChartPositionGraph
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonOrgChartPositionGraphCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonOrgChartPositionGraphReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonOrgChartPositionGraphCreateReadResponse(error = readResponse.error, orgChartPositionGraph = readResponse.orgChartPositionGraph)
    }
}
