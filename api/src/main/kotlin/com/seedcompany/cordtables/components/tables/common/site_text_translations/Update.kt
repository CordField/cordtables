package com.seedcompany.cordtables.components.tables.common.site_text_translations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class SiteTextTranslationUpdateInput(
  val site_text: Int,
  val language: Int,
  val newValue: String?
)

data class SiteTextTranslationUpdateRequest(
  val token: String,
  val site_text_translation: SiteTextTranslationUpdateInput,
)

data class SiteTextTranslationUpdateResponse(
  val error: ErrorType,
  val id: String? = null,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextTranslationUpdate")
class Update(
  @Autowired
  val util: Utility,
  @Autowired
  val ds: DataSource,
  @Autowired
  val create: Create
) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  @PostMapping("common-site-text-translations/update")
  @ResponseBody
  fun updateHandler(@RequestBody req: SiteTextTranslationUpdateRequest): SiteTextTranslationUpdateResponse {

    if (req.token == null) return SiteTextTranslationUpdateResponse(ErrorType.TokenNotFound)
    if (req.site_text_translation.language == null || req.site_text_translation.site_text == null) return SiteTextTranslationUpdateResponse(ErrorType.MissingId)
    var id: String? = null;

    try {
      id = jdbcTemplate.queryForObject(
        """
          select id from common.site_text_translations where language = ? and site_text = ?;
      """.trimIndent(),
        String::class.java,
        req.site_text_translation.language,
        req.site_text_translation.site_text
      )
    } catch(e: org.springframework.dao.EmptyResultDataAccessException) {
      // do nothing
    } catch (e: Exception) {
      return SiteTextTranslationUpdateResponse(ErrorType.UnknownError)
    }

    if (id == null) {
      val createResponse = create.createHandler(SiteTextTranslationCreateRequest(
        token = req.token,
        site_text_translation = SiteTextTranslationInput(
          language = req.site_text_translation.language,
          site_text = req.site_text_translation.site_text,
          translation = req.site_text_translation.newValue
        )
      ))
      return SiteTextTranslationUpdateResponse(createResponse.error, createResponse.id)
    } else {
      if (req.site_text_translation.newValue != null) util.updateField(
        token = req.token,
        table = "common.site_text_translations",
        column = "translation",
        id = id,
        value = req.site_text_translation.newValue,
      )
    }
    return SiteTextTranslationUpdateResponse(ErrorType.NoError, id)
  }
}
