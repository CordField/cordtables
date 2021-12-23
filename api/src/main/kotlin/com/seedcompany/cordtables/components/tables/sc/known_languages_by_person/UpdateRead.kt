package com.seedcompany.cordtables.components.tables.sc.known_languages_by_person

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.ScKnownLanguagesByPersonReadRequest
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.ScKnownLanguagesByPersonUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.knownLanguagesByPerson
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.knownLanguagesByPersonInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScKnownLanguagesByPersonUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScKnownLanguagesByPersonUpdateReadResponse(
    val error: ErrorType,
    val knownLanguagesByPerson: knownLanguagesByPerson? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScKnownLanguagesByPersonUpdateRead")
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
    @PostMapping("sc-known-languages-by-person/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScKnownLanguagesByPersonUpdateReadRequest): ScKnownLanguagesByPersonUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScKnownLanguagesByPersonUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScKnownLanguagesByPersonUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScKnownLanguagesByPersonReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScKnownLanguagesByPersonUpdateReadResponse(error = readResponse.error, readResponse.knownLanguagesByPerson)
    }
}
