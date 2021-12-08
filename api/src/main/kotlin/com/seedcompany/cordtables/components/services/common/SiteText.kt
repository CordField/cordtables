package com.seedcompany.cordtables.components.services.sil

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.services.SiteTextService
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

data class SiteTextInitRequest(
  val token: String? = null,
)

data class SiteTextInitResponse(
  val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextService")
class SiteText (
    @Autowired
    val util: Utility,
    @Autowired
    val siteTextService: SiteTextService,
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

    @PostMapping("services/common/site-text/init")
    @ResponseBody
    fun initHandler(@RequestBody req: SiteTextInitRequest): SiteTextInitResponse {

      if (req.token == null) return SiteTextInitResponse(ErrorType.InputMissingToken)

      if(!util.isAdmin(req.token)) return SiteTextInitResponse(ErrorType.AdminOnly)

      try {
        siteTextService.init(req.token)
      } catch(ex: Exception) {
        return SiteTextInitResponse(ErrorType.UnknownError)
      }
      return SiteTextInitResponse(ErrorType.NoError)
    }
}
