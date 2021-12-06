package com.seedcompany.cordtables.components.services.sil

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.core.DatabaseVersionControl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

data class SilLoaderRequest(
    val token: String? = null,
)

data class SilLoaderResponse(
    val error: ErrorType
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("SilLoaderService")
class SilLoader (
  @Autowired
    val vc: DatabaseVersionControl,
  @Autowired
    val util: Utility,
){
    @PostMapping("services/sil/load")
    @ResponseBody
    fun loadHandler(@RequestBody req: SilLoaderRequest): SilLoaderResponse {
      if (req.token == null) return SilLoaderResponse(ErrorType.InputMissingToken)

      if(util.isAdmin(req.token)) return SilLoaderResponse(ErrorType.AdminOnly)

      try {
            vc.loadSilData()
        } catch(ex: Exception) {
            return SilLoaderResponse(ErrorType.SQLUpdateError)
        }
        return SilLoaderResponse(ErrorType.NoError)
    }
}
