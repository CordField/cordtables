package com.seedcompany.cordtables.components.pages.translator

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.pages.partners.PartnersResponse
import com.seedcompany.cordtables.components.tables.common.site_text_strings.CommonSiteTextStringListResponse
import com.seedcompany.cordtables.components.tables.common.site_text_strings.SiteTextString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("TranslatorController")
class Controller (
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
    @PostMapping("page/translator/read")
    @ResponseBody
    fun list(@RequestBody req: TranslatorReadRequest): TranslatorReadResponse {
      var data: MutableList<TranslatorData> = mutableListOf()
      if (req.token == null) return TranslatorReadResponse(ErrorType.TokenNotFound)

      val paramSource = MapSqlParameterSource()

      val query = """
        select sts.id, sts.english, sts.comment, stt.translation
        from common.site_text_strings as sts
        left join common.site_text_translations as stt on sts.id = stt.site_text and stt.language = :language
       """.trimIndent()

      paramSource.addValue("language", req.language)

      try {
        val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
        while (jdbcResult.next()) {

          var id: String? = jdbcResult.getString("id")
          if (jdbcResult.wasNull()) id = null

          var english: String? = jdbcResult.getString("english")
          if (jdbcResult.wasNull()) english = null

          var comment: String? = jdbcResult.getString("comment")
          if (jdbcResult.wasNull()) comment = null

          var translation: String? = jdbcResult.getString("translation")
          if (jdbcResult.wasNull()) translation = null

          data.add(
            TranslatorData(
              id = id!!,
              english = english!!,
              comment = comment,
              translation = translation
            )
          )
        }
      } catch (e: SQLException) {
        println("error while listing ${e.message}")
        return TranslatorReadResponse(ErrorType.SQLReadError, mutableListOf())
      }

      return TranslatorReadResponse(ErrorType.NoError, data);
    }
}
