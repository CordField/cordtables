package com.seedcompany.cordtables.components.tables.sc.project_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectLocationsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProjectLocationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectLocationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-project-locations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScProjectLocationsUpdateRequest): ScProjectLocationsUpdateResponse {

        if (req.token == null) return ScProjectLocationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScProjectLocationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScProjectLocationsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "active" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_locations",
                    column = "active",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "change_to_plan" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_locations",
                    column = "change_to_plan",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "location" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_locations",
                    column = "location",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "project" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_locations",
                    column = "project",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_locations",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.project_locations",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScProjectLocationsUpdateResponse(ErrorType.NoError)
    }

}
