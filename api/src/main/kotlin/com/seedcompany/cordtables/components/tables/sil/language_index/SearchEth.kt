package com.seedcompany.cordtables.components.tables.sil.language_index

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class SilLanguageIndexSearchEthRequest(
  val lang: String,
)

data class SilLanguageIndexSearchEthResponse(
  val error: ErrorType,
  val id: String? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilLanguageIndexSearchEth")
class SearchEth(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {

  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("sil/language-index/search-eth")
  @ResponseBody
  fun listHandler(@RequestBody req: SilLanguageIndexSearchEthRequest): SilLanguageIndexSearchEthResponse {
    if (req.lang == null) return SilLanguageIndexSearchEthResponse(ErrorType.InputMissingColumn)

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("lang", req.lang)

    val query = """
      select id
      from sil.language_index
      where lang = :lang
      and name_type = 'L'
      limit 1
    """.trimIndent()

    var id: String? = null

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      if (jdbcResult.next()) {
        id = jdbcResult.getString("id")
      }
    } catch (e: SQLException) {
      println("error while searching ${e.message}")
      return SilLanguageIndexSearchEthResponse(ErrorType.SQLReadError, id)
    }

    return SilLanguageIndexSearchEthResponse(ErrorType.NoError, id)
  }
}
