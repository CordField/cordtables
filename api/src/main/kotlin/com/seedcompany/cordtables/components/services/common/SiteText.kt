package com.seedcompany.cordtables.components.services.sil

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.core.DatabaseVersionControl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

data class SiteTextUtilGetHexRequest(
        val name: String,
)

data class SiteTextUtilGetHexResponse(
        val error: ErrorType,
        val hex: String? = null,
)

data class SiteTextUtilGetStringRequest(
        val hex: String,
)

data class SiteTextUtilGetStringResponse(
        val error: ErrorType,
        val name: String? = null,
)


@Controller("CommonSiteTextService")
class SiteText (
        @Autowired
        val util: Utility,
){
    @PostMapping("services/common/site-text/utils/string2hex")
    @ResponseBody
    fun string2hexHandler(@RequestBody req: SiteTextUtilGetHexRequest): SiteTextUtilGetHexResponse {
        var result: String? = ""
        try {
            result = util.convertStringToHex(req.name)
        } catch(ex: Exception) {
            return SiteTextUtilGetHexResponse(ErrorType.UnknownError)
        }
        return SiteTextUtilGetHexResponse(ErrorType.NoError, result)
    }

    @PostMapping("services/common/site-text/utils/hex2string")
    @ResponseBody
    fun hex2stringHanlder(@RequestBody req: SiteTextUtilGetStringRequest): SiteTextUtilGetStringResponse {
        var result: String? = ""
        try {
            result = util.convertHexToString(req.hex)
        } catch(ex: Exception) {
            return SiteTextUtilGetStringResponse(ErrorType.UnknownError)
        }
        return SiteTextUtilGetStringResponse(ErrorType.NoError, result)
    }
}