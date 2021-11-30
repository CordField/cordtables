package com.seedcompany.cordtables.components.tables.common.stages

import com.seedcompany.cordtables.components.tables.common.stages.CommonStagesUpdateRequest
import com.seedcompany.cordtables.components.tables.common.stages.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.stages.CommonStagesUpdateResponse
import com.seedcompany.cordtables.components.tables.common.stages.stageInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStagesUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonStagesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonStagesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-stages/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonStagesUpdateRequest): CommonStagesUpdateResponse {

        if (req.token == null) return CommonStagesUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonStagesUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonStagesUpdateResponse(ErrorType.MissingId)

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
                    table = "common.stages",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stages",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonStagesUpdateResponse(ErrorType.NoError)
    }

}