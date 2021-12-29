package com.seedcompany.cordtables.components.tables.sc.known_languages_by_person

import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.ScKnownLanguagesByPersonUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.ScKnownLanguagesByPersonUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.knownLanguagesByPersonInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScKnownLanguagesByPersonUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScKnownLanguagesByPersonUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScKnownLanguagesByPersonUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/known-languages-by-person/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScKnownLanguagesByPersonUpdateRequest): ScKnownLanguagesByPersonUpdateResponse {

        if (req.token == null) return ScKnownLanguagesByPersonUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScKnownLanguagesByPersonUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScKnownLanguagesByPersonUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.known_languages_by_person",
                    column = "person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "known_language" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.known_languages_by_person",
                    column = "known_language",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.known_languages_by_person",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.known_languages_by_person",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScKnownLanguagesByPersonUpdateResponse(ErrorType.NoError)
    }

}
