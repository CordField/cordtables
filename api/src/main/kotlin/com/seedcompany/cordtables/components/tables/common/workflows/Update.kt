package com.seedcompany.cordtables.components.tables.common.workflows

import com.seedcompany.cordtables.components.tables.common.workflows.CommonWorkflowsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.workflows.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.workflows.CommonWorkflowsUpdateResponse
import com.seedcompany.cordtables.components.tables.common.workflows.workflowInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonWorkflowsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonWorkflowsUpdateResponse(
    val error: ErrorType,
)



@Controller("CommonWorkflowsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-workflows/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonWorkflowsUpdateRequest): CommonWorkflowsUpdateResponse {

        if (req.token == null) return CommonWorkflowsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonWorkflowsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonWorkflowsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "title" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stages",
                    column = "title",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.workflows",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.workflows",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonWorkflowsUpdateResponse(ErrorType.NoError)
    }

}