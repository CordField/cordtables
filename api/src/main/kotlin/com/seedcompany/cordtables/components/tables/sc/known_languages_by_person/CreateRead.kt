package com.seedcompany.cordtables.components.tables.sc.known_languages_by_person

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.*
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.ScKnownLanguagesByPersonCreateRequest
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScKnownLanguagesByPersonCreateReadRequest(
    val token: String? = null,
    val knownLanguagesByPerson: knownLanguagesByPersonInput,
)

data class ScKnownLanguagesByPersonCreateReadResponse(
    val error: ErrorType,
    val knownLanguagesByPerson: knownLanguagesByPerson? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScKnownLanguagesByPersonCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("sc-known-languages-by-person/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScKnownLanguagesByPersonCreateReadRequest): ScKnownLanguagesByPersonCreateReadResponse {

        val createResponse = create.createHandler(
            ScKnownLanguagesByPersonCreateRequest(
                token = req.token,
                knownLanguagesByPerson = req.knownLanguagesByPerson
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScKnownLanguagesByPersonCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScKnownLanguagesByPersonReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScKnownLanguagesByPersonCreateReadResponse(error = readResponse.error, knownLanguagesByPerson = readResponse.knownLanguagesByPerson)
    }
}