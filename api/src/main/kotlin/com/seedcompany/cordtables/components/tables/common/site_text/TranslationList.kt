package com.seedcompany.cordtables.components.tables.common.site_text

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.services.SiteTextTranslation
import com.seedcompany.cordtables.services.SiteTextService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

data class CommonSiteTextTranslationListResponse (
        val error: ErrorType,
        val data: MutableList<SiteTextTranslation>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonSiteTextTranslationList")
class TranslationList(
        @Autowired
        val siteTextService: SiteTextService,
) {

    @PostMapping("common/site-texts/translations/list")
    @ResponseBody
    fun listHandler(): CommonSiteTextTranslationListResponse {
        var serviceResponse =  siteTextService.getSiteTextTranslations()

        return CommonSiteTextTranslationListResponse(serviceResponse.error, serviceResponse.data)
    }
}