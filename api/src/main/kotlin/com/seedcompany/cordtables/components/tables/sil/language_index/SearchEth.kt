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
  val data: MutableList<Int>? = null
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

  @PostMapping("sil-language-index/search-eth")
  @ResponseBody
  fun listHandler(@RequestBody req:SilLanguageIndexSearchEthRequest): SilLanguageIndexSearchEthResponse {
    var data: MutableList<Int> = mutableListOf()
    if(req.lang == null) return SilLanguageIndexSearchEthResponse(ErrorType.InputMissingColumn)

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("lang", req.lang)

    val query = """
      select id
      from sil.language_index
      where lang = :lang
    """.trimIndent()

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var id: Int = jdbcResult.getInt("id")
        data.add(id)
      }
    } catch (e: SQLException) {
      println("error while searching ${e.message}")
      return SilLanguageIndexSearchEthResponse(ErrorType.SQLReadError, mutableListOf())
    }

    return SilLanguageIndexSearchEthResponse(ErrorType.NoError, data)
  }
}
