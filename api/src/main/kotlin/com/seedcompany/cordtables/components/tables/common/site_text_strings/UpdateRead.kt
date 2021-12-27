package com.seedcompany.cordtables.components.tables.common.site_text_strings

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class SiteTextStringUpdateReadRequest(
  val token: String,
  val site_text_string: SiteTextStringUpdateInput,
)

data class SiteTextStringUpdateReadResponse(
  val error: ErrorType,
  val site_text_string: SiteTextString? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextStringUpdateRead")
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
  @PostMapping("common-site-text-strings/update-read")
  @ResponseBody
  fun updateReadHandler(@RequestBody req: SiteTextStringUpdateReadRequest): SiteTextStringUpdateReadResponse {

    val updateResponse = update.updateHandler(
      SiteTextStringUpdateRequest(
        token = req.token,
        site_text_string = req.site_text_string,
      )
    )
    if (updateResponse.error != ErrorType.NoError) {
      return SiteTextStringUpdateReadResponse(updateResponse.error)
    }

    val readResponse = read.readHandler(
      SiteTextStringReadRequest(
        token = req.token,
        id = req.site_text_string!!.id
      )
    )

    return SiteTextStringUpdateReadResponse(error = readResponse.error, readResponse.site_text_string)
  }
}
