package com.seedcompany.cordtables.components.tables.sc.language_ex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class LanguageExCreateReadRequest(
    val token: String? = null,
    val language: LanguageExInput,
)

data class LanguageExCreateReadResponse(
    val error: ErrorType,
    val language: LanguageEx? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SCLanguageExCreateRead")
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
    fun createReadHandler(@RequestBody req: LanguageExCreateReadRequest): LanguageExCreateReadResponse {

        val createResponse = create.createHandler(
            CreateLanguageExRequest(
                token = req.token,
                language = req.language
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return LanguageExCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            LanguageExReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return LanguageExCreateReadResponse(error = readResponse.error, language = readResponse.languageEx)
    }
}
