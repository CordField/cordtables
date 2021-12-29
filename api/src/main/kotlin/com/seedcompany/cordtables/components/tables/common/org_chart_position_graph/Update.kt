package com.seedcompany.cordtables.components.tables.common.org_chart_position_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrgChartPositionGraphUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonOrgChartPositionGraphUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrgChartPositionGraphUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-org-chart-position-graph/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonOrgChartPositionGraphUpdateRequest): CommonOrgChartPositionGraphUpdateResponse {

        if (req.token == null) return CommonOrgChartPositionGraphUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonOrgChartPositionGraphUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonOrgChartPositionGraphUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "from_position" -> {
                util.updateField(
                    token = req.token,
                    table = "common.org_chart_position_graph",
                    column = "from_position",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "to_position" -> {
                util.updateField(
                    token = req.token,
                    table = "common.org_chart_position_graph",
                    column = "to_position",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "relationship_type" -> {
                util.updateField(
                    token = req.token,
                    table = "common.org_chart_position_graph",
                    column = "relationship_type",
                    id = req.id,
                    value = req.value,
                    cast = "::common.position_relationship_types"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.org_chart_position_graph",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.org_chart_position_graph",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return CommonOrgChartPositionGraphUpdateResponse(ErrorType.NoError)
    }

}
