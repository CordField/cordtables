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

data class CommonSiteTextTranslationUpdateReadRequest(
        val token: String,
        val site_text_translation: CommonSiteTextTranslationInput,
)

data class CommonSiteTextTranslationUpdateReadResponse(
        val error: ErrorType,
        val site_text_translation: CommonSiteTextTranslation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonSiteTextTranslationUpdateRead")
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
    @PostMapping("common-site-text-translations/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonSiteTextTranslationUpdateReadRequest): CommonSiteTextTranslationUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonSiteTextTranslationUpdateRequest(
                token = req.token,
                site_text_translation = req.site_text_translation,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonSiteTextTranslationUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonSiteTextTranslationReadRequest(
                token = req.token,
                id = req.site_text_translation.id
            )
        )

        return CommonSiteTextTranslationUpdateReadResponse(error = readResponse.error, readResponse.site_text_translation)
    }
}
