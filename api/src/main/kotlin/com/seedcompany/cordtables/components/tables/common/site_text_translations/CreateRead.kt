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

data class SiteTextTranslationCreateReadRequest(
  val token: String? = null,
  val site_text_translation: SiteTextTranslationInput,
)

data class SiteTextTranslationCreateReadResponse(
  val error: ErrorType,
  val site_text_translation: SiteTextTranslation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextTranslationCreateRead")
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
  @PostMapping("common-site-text-translations/create-read")
  @ResponseBody
  fun createReadHandler(@RequestBody req: SiteTextTranslationCreateReadRequest): SiteTextTranslationCreateReadResponse {

    val createResponse = create.createHandler(
      SiteTextTranslationCreateRequest(
        token = req.token,
        site_text_translation = req.site_text_translation
      )
    )

    if (createResponse.error != ErrorType.NoError) {
      return SiteTextTranslationCreateReadResponse(error = createResponse.error)
    }

    val readResponse = read.readHandler(
      SiteTextTranslationReadRequest(
        token = req.token,
        id = createResponse!!.id
      )
    )

    return SiteTextTranslationCreateReadResponse(error = readResponse.error, site_text_translation = readResponse.site_text_translation)
  }
}
