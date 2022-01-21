package com.seedcompany.cordtables.components.tables.common.site_text_languages

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

data class SiteTextLanguageCreateRequest(
  val token: String? = null,
  val language: String,
)

data class SiteTextLanguageCreateResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextLanguageCreate")
class Create(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  @PostMapping("common/site-text-languages/create")
  @ResponseBody
  fun createHandler(@RequestBody req: SiteTextLanguageCreateRequest): SiteTextLanguageCreateResponse {

    if (req.token == null) return SiteTextLanguageCreateResponse(error = ErrorType.InputMissingToken, null)
    if (req.language == null) return SiteTextLanguageCreateResponse(error = ErrorType.InputMissingColumn, null)

    try {
      val id: String = jdbcTemplate.queryForObject(
        """
            insert into common.site_text_languages(
                language,
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
            values(
                ?::uuid,
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
        req.language,
        req.token,
        req.token,
        req.token,
        util.adminGroupId
      )

      return SiteTextLanguageCreateResponse(error = ErrorType.NoError, id = id)

    } catch (e: SQLException) {
      println(e.message)
      return SiteTextLanguageCreateResponse(ErrorType.SQLInsertError, null)
    }
  }
}
