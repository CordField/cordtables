package com.seedcompany.cordtables.components.tables.common.org_chart_positions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.*
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.CommonOrgChartPositionsCreateRequest
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrgChartPositionsCreateReadRequest(
    val token: String? = null,
    val orgChartPosition: orgChartPositionInput,
)

data class CommonOrgChartPositionsCreateReadResponse(
    val error: ErrorType,
    val orgChartPosition: orgChartPosition? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonOrgChartPositionsCreateRead")
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
    @PostMapping("common-org-chart-positions/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonOrgChartPositionsCreateReadRequest): CommonOrgChartPositionsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonOrgChartPositionsCreateRequest(
                token = req.token,
                orgChartPosition = req.orgChartPosition
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonOrgChartPositionsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonOrgChartPositionsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonOrgChartPositionsCreateReadResponse(error = readResponse.error, orgChartPosition = readResponse.orgChartPosition)
    }
}