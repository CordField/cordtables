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

data class SiteTextTranslationRecordUpdateReadRequest(
  val token: String,
  val site_text_translation: SiteTextTranslationRecordUpdateInput,
)

data class SiteTextTranslationRecordUpdateReadResponse(
  val error: ErrorType,
  val site_text_translation: SiteTextTranslation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextTranslationRecordUpdateRead")
class RecordUpdateRead(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val update: RecordUpdate,

  @Autowired
  val read: Read,
) {
  @PostMapping("common/site-text-translations/record/update-read")
  @ResponseBody
  fun updateReadHandler(@RequestBody req: SiteTextTranslationRecordUpdateReadRequest): SiteTextTranslationRecordUpdateReadResponse {

    val updateResponse = update.updateHandler(
      SiteTextTranslationRecordUpdateRequest(
        token = req.token,
        site_text_translation = req.site_text_translation,
      )
    )
    if (updateResponse.error != ErrorType.NoError) {
      return SiteTextTranslationRecordUpdateReadResponse(updateResponse.error)
    }

    val readResponse = read.readHandler(
      SiteTextTranslationReadRequest(
        token = req.token,
        id = req.site_text_translation.id
      )
    )

    return SiteTextTranslationRecordUpdateReadResponse(error = readResponse.error, readResponse.site_text_translation)
  }
}
