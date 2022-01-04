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

data class SiteTextStringCreateReadRequest(
  val token: String? = null,
  val site_text_string: SiteTextStringInput,
)

data class SiteTextStringCreateReadResponse(
  val error: ErrorType,
  val site_text_string: SiteTextString? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextStringCreateRead")
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
  @PostMapping("common/site-text-strings/create-read")
  @ResponseBody
  fun createReadHandler(@RequestBody req: SiteTextStringCreateReadRequest): SiteTextStringCreateReadResponse {

    val createResponse = create.createHandler(
      SiteTextStringCreateRequest(
        token = req.token,
        site_text_string = req.site_text_string
      )
    )

    if (createResponse.error != ErrorType.NoError) {
      return SiteTextStringCreateReadResponse(error = createResponse.error)
    }

    val readResponse = read.readHandler(
      SiteTextStringReadRequest(
        token = req.token,
        id = createResponse!!.id
      )
    )

    return SiteTextStringCreateReadResponse(error = readResponse.error, site_text_string = readResponse.site_text_string)
  }
}
