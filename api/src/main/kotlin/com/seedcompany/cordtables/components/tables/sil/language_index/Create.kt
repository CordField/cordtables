package com.seedcompany.cordtables.components.tables.sil.language_index

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

data class SilLanguageIndexCreateRequest(
  val token: String? = null,
  val language: LanguageIndexInput,
)

data class SilLanguageIndexCreateResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("SilLanguageIndexCreate")
class Create(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  @PostMapping("sil/language-index/create")
  @ResponseBody
  fun createHandler(@RequestBody req: SilLanguageIndexCreateRequest): SilLanguageIndexCreateResponse {

    if (req.token == null) return SilLanguageIndexCreateResponse(error = ErrorType.InputMissingToken, null)

    // create row with required fields, use id to update cells afterwards one by one
    val id = jdbcTemplate.queryForObject(
      """
            insert into sil.language_index(id, lang, country, name_type, name, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?,
                    ?::sil.language_name_type,
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
                    ?
                )
            returning id;
        """.trimIndent(),
      String::class.java,
      req.language.id,
      req.language.lang,
      req.language.country,
      req.language.name_type,
      req.language.name,
      req.token,
      req.token,
      req.token,
      util.adminGroupId()
    )

    return SilLanguageIndexCreateResponse(error = ErrorType.NoError, id = id)
  }


}
