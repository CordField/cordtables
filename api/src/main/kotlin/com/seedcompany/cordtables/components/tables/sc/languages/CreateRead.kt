package com.seedcompany.cordtables.components.tables.sc.languages

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguagesCreateReadRequest(
    val token: String? = null,
    val language: LanguageInput,
)

data class ScLanguagesCreateReadResponse(
    val error: ErrorType,
    val language: Language? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScLanguagesCreateRead")
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
    @PostMapping("sc-languages/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScLanguagesCreateReadRequest): ScLanguagesCreateReadResponse {

        val createResponse = create.createHandler(
            ScLanguagesCreateRequest(
                token = req.token,
                language = req.language
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScLanguagesCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScLanguagesReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScLanguagesCreateReadResponse(error = readResponse.error, language = readResponse.language)
    }
}
