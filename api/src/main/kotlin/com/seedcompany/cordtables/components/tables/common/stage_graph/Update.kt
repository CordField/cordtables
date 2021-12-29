package com.seedcompany.cordtables.components.tables.common.stage_graph

import com.seedcompany.cordtables.components.tables.common.stage_graph.CommonStageGraphUpdateRequest
import com.seedcompany.cordtables.components.tables.common.stage_graph.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.stage_graph.CommonStageGraphUpdateResponse
import com.seedcompany.cordtables.components.tables.common.stage_graph.stageGraphInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageGraphUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonStageGraphUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageGraphUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/stage-graph/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonStageGraphUpdateRequest): CommonStageGraphUpdateResponse {

        if (req.token == null) return CommonStageGraphUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonStageGraphUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonStageGraphUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "from_stage" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_graph",
                    column = "from_stage",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "to_stage" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_graph",
                    column = "to_stage",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_graph",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_graph",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonStageGraphUpdateResponse(ErrorType.NoError)
    }

}
