package com.seedcompany.cordtables.components.tables.common.org_chart_positions

import com.seedcompany.cordtables.components.tables.common.org_chart_positions.CommonOrgChartPositionsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.CommonOrgChartPositionsUpdateResponse
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.orgChartPositionInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrgChartPositionsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonOrgChartPositionsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonOrgChartPositionsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-org-chart-positions/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonOrgChartPositionsUpdateRequest): CommonOrgChartPositionsUpdateResponse {

        if (req.token == null) return CommonOrgChartPositionsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonOrgChartPositionsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonOrgChartPositionsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "organization" -> {
                util.updateField(
                    token = req.token,
                    table = "common.org_chart_positions",
                    column = "organization",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.org_chart_positions",
                    column = "name",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.org_chart_positions",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.org_chart_positions",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonOrgChartPositionsUpdateResponse(ErrorType.NoError)
    }

}