package com.seedcompany.cordtables.components.tables.sc.ethnologue

import com.seedcompany.cordtables.components.tables.sc.ethnologue.ScEthnologueUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.ethnologue.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ScEthnologueUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ethnologueInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScEthnologueUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScEthnologueUpdateResponse(
    val error: ErrorType,
)



@Controller("ScEthnologueUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-ethnologue/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScEthnologueUpdateRequest): ScEthnologueUpdateResponse {

        if (req.token == null) return ScEthnologueUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScEthnologueUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScEthnologueUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "neo4j_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.ethnologue",
                    column = "neo4j_id",
                    id = req.id,
                    value = req.value,
                )
            }
            "language_index" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.ethnologue",
                    column = "language_index",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "code" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.ethnologue",
                    column = "code",
                    id = req.id,
                    value = req.value,
                )
            }
            "language_name" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.ethnologue",
                    column = "language_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "population" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.ethnologue",
                    column = "population",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "provisional_code" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.ethnologue",
                    column = "provisional_code",
                    id = req.id,
                    value = req.value,
                )
            }
            "sensitivity" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.ethnologue",
                    column = "sensitivity",
                    id = req.id,
                    value = req.value,
                    cast = "::common.sensitivity"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.ethnologue",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.ethnologue",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScEthnologueUpdateResponse(ErrorType.NoError)
    }

}