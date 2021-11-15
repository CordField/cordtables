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

data class CommonSiteTextTranslationUpdateRequest(
    val token: String,
    val site_text_translation: CommonSiteTextTranslationInput,
)

data class CommonSiteTextTranslationUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonSiteTextTranslationUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-site-text-translations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonSiteTextTranslationUpdateRequest): CommonSiteTextTranslationUpdateResponse {

        if (req.token == null) return CommonSiteTextTranslationUpdateResponse(ErrorType.TokenNotFound)
        if (req.site_text_translation.id == null) return CommonSiteTextTranslationUpdateResponse(ErrorType.MissingId)

        if (req.site_text_translation.site_text_id != null) util.updateField(
            token = req.token,
            table = "common.site_text_translations",
            column = "site_text_id",
            id = req.site_text_translation.id!!,
            value = req.site_text_translation.site_text_id,
        )

        if (req.site_text_translation.text_id != null) util.updateField(
            token = req.token,
            table = "common.site_text_translations",
            column = "text_id",
            id = req.site_text_translation.id!!,
            value = req.site_text_translation.text_id,
        )

        if (req.site_text_translation.text_translation != null) util.updateField(
            token = req.token,
            table = "common.site_text_translations",
            column = "text_translation",
            id = req.site_text_translation.id!!,
            value = req.site_text_translation.text_translation,
        )

        return CommonSiteTextTranslationUpdateResponse(ErrorType.NoError)
    }

}
