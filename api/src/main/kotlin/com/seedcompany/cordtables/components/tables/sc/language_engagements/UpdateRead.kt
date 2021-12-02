package com.seedcompany.cordtables.components.tables.sc.language_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.language_engagements.ScLanguageEngagementsReadRequest
import com.seedcompany.cordtables.components.tables.sc.language_engagements.ScLanguageEngagementsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.language_engagements.languageEngagement
import com.seedcompany.cordtables.components.tables.sc.language_engagements.languageEngagementInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguageEngagementsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScLanguageEngagementsUpdateReadResponse(
    val error: ErrorType,
    val languageEngagement: languageEngagement? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageEngagementsUpdateRead")
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
    @PostMapping("sc-language-engagements/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScLanguageEngagementsUpdateReadRequest): ScLanguageEngagementsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScLanguageEngagementsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScLanguageEngagementsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScLanguageEngagementsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScLanguageEngagementsUpdateReadResponse(error = readResponse.error, readResponse.languageEngagement)
    }
}