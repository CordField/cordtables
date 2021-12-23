package com.seedcompany.cordtables.components.tables.common.org_chart_position_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.CommonOrgChartPositionGraphReadRequest
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.CommonOrgChartPositionGraphUpdateRequest
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.orgChartPositionGraph
import com.seedcompany.cordtables.components.tables.common.org_chart_position_graph.orgChartPositionGraphInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrgChartPositionGraphUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonOrgChartPositionGraphUpdateReadResponse(
    val error: ErrorType,
    val orgChartPositionGraph: orgChartPositionGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrgChartPositionGraphUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("common-org-chart-position-graph/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonOrgChartPositionGraphUpdateReadRequest): CommonOrgChartPositionGraphUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonOrgChartPositionGraphUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonOrgChartPositionGraphUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonOrgChartPositionGraphReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonOrgChartPositionGraphUpdateReadResponse(error = readResponse.error, readResponse.orgChartPositionGraph)
    }
}
