package com.seedcompany.cordtables.components.tables.common.site_text_strings

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

data class SiteTextStringCreateRequest(
  val token: String? = null,
  val site_text_string: SiteTextStringInput,
)

data class SiteTextStringCreateResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextStringCreate")
class Create(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  @PostMapping("common/site-text-strings/create")
  @ResponseBody
  fun createHandler(@RequestBody req: SiteTextStringCreateRequest): SiteTextStringCreateResponse {

    if (req.token == null) return SiteTextStringCreateResponse(error = ErrorType.InputMissingToken, null)
    if (req.site_text_string.english == null) return SiteTextStringCreateResponse(error = ErrorType.InputMissingColumn, null)

    try {
      val id = jdbcTemplate.queryForObject(
        """
            insert into common.site_text_strings(
                english,
                comment,
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
            values(
                ?,
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
        req.site_text_string.english,
        req.site_text_string.comment,
        req.token,
        req.token,
        req.token,
        util.adminGroupId
      )

      return SiteTextStringCreateResponse(error = ErrorType.NoError, id = id)

    } catch (e: SQLException) {
      println(e.message)
      return SiteTextStringCreateResponse(ErrorType.SQLInsertError, null)
    }
  }
}
