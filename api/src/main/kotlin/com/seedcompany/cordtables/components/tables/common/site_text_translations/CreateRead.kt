package com.seedcompany.cordtables.components.tables.common.site_text_translations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonSiteTextTranslationCreateReadRequest(
        val token: String? = null,
        val site_text_translation: CommonSiteTextTranslationInput,
)

data class CommonSiteTextTranslationCreateReadResponse(
        val error: ErrorType,
        val site_text_translation: CommonSiteTextTranslation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonSiteTextTranslationCreateRead")
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
    @PostMapping("common-site-text-translations/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonSiteTextTranslationCreateReadRequest): CommonSiteTextTranslationCreateReadResponse {

        val createResponse = create.createHandler(
                CommonSiteTextTranslationCreateRequest(
                        token = req.token,
                        site_text_translation = req.site_text_translation
                )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonSiteTextTranslationCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonSiteTextTranslationReadRequest(
                    token = req.token,
                    id = createResponse!!.id
            )
        )

        return CommonSiteTextTranslationCreateReadResponse(error = readResponse.error, site_text_translation = readResponse.site_text_translation)
    }
}