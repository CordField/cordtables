package com.seedcompany.cordtables.components.tables.common.org_chart_positions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.CommonOrgChartPositionsReadRequest
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.CommonOrgChartPositionsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.orgChartPosition
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.orgChartPositionInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrgChartPositionsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonOrgChartPositionsUpdateReadResponse(
    val error: ErrorType,
    val orgChartPosition: orgChartPosition? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrgChartPositionsUpdateRead")
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
    @PostMapping("common/org-chart-positions/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonOrgChartPositionsUpdateReadRequest): CommonOrgChartPositionsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonOrgChartPositionsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonOrgChartPositionsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonOrgChartPositionsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonOrgChartPositionsUpdateReadResponse(error = readResponse.error, readResponse.orgChartPosition)
    }
}
