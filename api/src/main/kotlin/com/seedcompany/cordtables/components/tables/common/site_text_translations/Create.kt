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
import java.sql.SQLException
import javax.sql.DataSource

data class SiteTextTranslationCreateRequest(
  val token: String? = null,
  val site_text_translation: SiteTextTranslationInput,
)

data class SiteTextTranslationCreateResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextTranslationCreate")
class Create(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  @PostMapping("common/site-text-translations/create")
  @ResponseBody
  fun createHandler(@RequestBody req: SiteTextTranslationCreateRequest): SiteTextTranslationCreateResponse {

    if (req.token == null) return SiteTextTranslationCreateResponse(error = ErrorType.InputMissingToken, null)
    if (req.site_text_translation.language == null) return SiteTextTranslationCreateResponse(error = ErrorType.InputMissingColumn, null)

    try {
      val id = jdbcTemplate.queryForObject(
        """
            insert into common.site_text_translations(
                language,
                site_text,
                translation,
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
            values(
                ?::uuid,
                ?::uuid,
                ?,
                (
                  select person 
                  from admin.tokens 
                  where token = ?
                ),
                (
                  select person 
                  from admin.tokens 
                  where token = ?
                ),
                (
                  select person 
                  from admin.tokens 
                  where token = ?
                ),
                ?::uuid
            )
            returning id;
            """.trimIndent(),
        String::class.java,
        req.site_text_translation.language,
        req.site_text_translation.site_text,
        req.site_text_translation.translation,
        req.token,
        req.token,
        req.token,
        util.adminGroupId
      )

      return SiteTextTranslationCreateResponse(error = ErrorType.NoError, id = id)

    } catch (e: SQLException) {
      println(e.message)
      return SiteTextTranslationCreateResponse(ErrorType.SQLInsertError, null)
    }
  }
}
