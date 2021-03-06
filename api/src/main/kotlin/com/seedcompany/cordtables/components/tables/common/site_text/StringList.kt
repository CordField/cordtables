package com.seedcompany.cordtables.components.tables.common.site_text

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.services.SiteTextString
import com.seedcompany.cordtables.services.SiteTextService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

data class CommonSiteTextStringListResponse (
        val error: ErrorType,
        val data: MutableList<SiteTextString>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextStringList")
class StringList(
        @Autowired
        val siteTextService: SiteTextService,
) {

    @PostMapping("common/site-texts/strings/list")
    @ResponseBody
    fun listHandler(): CommonSiteTextStringListResponse {
        var serviceResponse =  siteTextService.getSiteTextStrings()

        return CommonSiteTextStringListResponse(serviceResponse.error, serviceResponse.data)
    }
}