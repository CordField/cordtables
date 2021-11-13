package com.seedcompany.cordtables.components.tables.sc.person_unavailabilities

import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.ScPersonUnavailabilitiesUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.ScPersonUnavailabilitiesUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.personUnavailabilityInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPersonUnavailabilitiesUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPersonUnavailabilitiesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScPersonUnavailabilitiesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-person-unavailabilities/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScPersonUnavailabilitiesUpdateRequest): ScPersonUnavailabilitiesUpdateResponse {

        if (req.token == null) return ScPersonUnavailabilitiesUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScPersonUnavailabilitiesUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScPersonUnavailabilitiesUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.person_unavailabilities",
                    column = "person",
                    id = req.id,
                    value = req.value,
                )
            }
            "period_start" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.person_unavailabilities",
                    column = "period_start",
                    id = req.id,
                    value = req.value,
                )
            }
            "period_end" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.person_unavailabilities",
                    column = "period_end",
                    id = req.id,
                    value = req.value,
                )
            }
            "description" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.person_unavailabilities",
                    column = "description",
                    id = req.id,
                    value = req.value,
                )
            }

            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.person_unavailabilities",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.person_unavailabilities",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return ScPersonUnavailabilitiesUpdateResponse(ErrorType.NoError)
    }

}