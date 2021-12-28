package com.seedcompany.cordtables.components.tables.sc.language_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.language_engagements.*
import com.seedcompany.cordtables.components.tables.sc.language_engagements.ScLanguageEngagementsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.language_engagements.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguageEngagementsCreateReadRequest(
    val token: String? = null,
    val languageEngagement: languageEngagementInput,
)

data class ScLanguageEngagementsCreateReadResponse(
    val error: ErrorType,
    val languageEngagement: languageEngagement? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageEngagementsCreateRead")
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
    @PostMapping("sc/language-engagements/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScLanguageEngagementsCreateReadRequest): ScLanguageEngagementsCreateReadResponse {

        val createResponse = create.createHandler(
            ScLanguageEngagementsCreateRequest(
                token = req.token,
                languageEngagement = req.languageEngagement
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScLanguageEngagementsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScLanguageEngagementsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScLanguageEngagementsCreateReadResponse(error = readResponse.error, languageEngagement = readResponse.languageEngagement)
    }
}
