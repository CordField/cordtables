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

data class SiteTextTranslationRecordUpdateInput(
  val id: String,
  val column: String,
  val newValue: String?
)

data class SiteTextTranslationRecordUpdateRequest(
  val token: String,
  val site_text_translation: SiteTextTranslationRecordUpdateInput,
)

data class SiteTextTranslationRecordUpdateResponse(
  val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextTranslationRecordUpdate")
class RecordUpdate(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("common/site-text-translation/record/update")
  @ResponseBody
  fun updateHandler(@RequestBody req: SiteTextTranslationRecordUpdateRequest): SiteTextTranslationRecordUpdateResponse {

    if (req.token == null) return SiteTextTranslationRecordUpdateResponse(ErrorType.TokenNotFound)
    if (req.site_text_translation.id == null) return SiteTextTranslationRecordUpdateResponse(ErrorType.MissingId)

    if (req.site_text_translation.column != null) util.updateField(
      token = req.token,
      table = "common.site_text_translations",
      column = req.site_text_translation.column,
      id = req.site_text_translation.id,
      value = req.site_text_translation.newValue,
    )
    return SiteTextTranslationRecordUpdateResponse(ErrorType.NoError)
  }
}
