package com.seedcompany.cordtables.components.tables.common.site_text

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.services.SiteTextLanguage
import com.seedcompany.cordtables.services.SiteTextService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

data class CommonSiteTextListResponse (
        val error: ErrorType,
        val data: MutableList<SiteTextLanguage>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonSiteTextList")
class List(
        @Autowired
        val siteTextService: SiteTextService,
) {

    @PostMapping("service/site-text-languages/list")
    @ResponseBody
    fun listHandler(): CommonSiteTextListResponse {
        var serviceResponse =  siteTextService.getSiteTextLanguages()

        return CommonSiteTextListResponse(serviceResponse.error, serviceResponse.data)
    }
}