package com.seedcompany.cordtables.components.tables.common.site_text_translations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.SQLException
import javax.sql.DataSource

data class SiteTextTranslationSearchRequest(
  val searchColumnName: String,
  val token: String,
  val searchKeyword: String = ""
)

data class SiteTextTranslationSearchResponse(
  val error: ErrorType,
  val data: MutableList<SiteTextTranslation>? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("SiteTextTranslationSearch")
class Search(
  @Autowired
  val util: Utility,
  @Autowired
  val secureList: GetSecureListQuery,
  @Autowired
  val ds: DataSource
){
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("common/site-text-translations/search")
  @ResponseBody
  fun searchHandler(@RequestBody req: SiteTextTranslationSearchRequest): SiteTextTranslationSearchResponse {
    if (req.token == null) return SiteTextTranslationSearchResponse(ErrorType.InputMissingToken)
//    if (!util.isAdmin(req.token)) return SiteTextStringSearchResponse(ErrorType.AdminOnly)
    var data: MutableList<SiteTextTranslation> = mutableListOf()
    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)
    val whereClause = "${req.searchColumnName} like '${req.searchKeyword}%'"
    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "common.site_text_translations",
        filter = "order by id",
        columns = arrayOf(
          "id",
          "language",
          "site_text",
          "translation",
          "created_at",
          "created_by",
          "modified_at",
          "modified_by",
          "owning_person",
          "owning_group",
        ),
        whereClause = whereClause
      )
    ).query
    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var id: String? = jdbcResult.getString("id")
        if (jdbcResult.wasNull()) id = null

        var language: String? = jdbcResult.getString("language")
        if (jdbcResult.wasNull()) language = null

        var site_text: String? = jdbcResult.getString("site_text")
        if(jdbcResult.wasNull()) site_text = null

        var translation: String? = jdbcResult.getString("translation")
        if (jdbcResult.wasNull()) translation = null

        var created_at: String? = jdbcResult.getString("created_at")
        if (jdbcResult.wasNull()) created_at = null

        var created_by: String? = jdbcResult.getString("created_by")
        if (jdbcResult.wasNull()) created_by = null

        var modified_at: String? = jdbcResult.getString("modified_at")
        if (jdbcResult.wasNull()) modified_at = null

        var modified_by: String? = jdbcResult.getString("modified_by")
        if (jdbcResult.wasNull()) modified_by = null

        var owning_person: String? = jdbcResult.getString("owning_person")
        if (jdbcResult.wasNull()) owning_person = null

        var owning_group: String? = jdbcResult.getString("owning_group")
        if (jdbcResult.wasNull()) owning_group = null

        data.add(
          SiteTextTranslation(
            id = id!!,
            language = language!!,
            site_text = site_text!!,
            translation = translation!!,
            created_at = created_at,
            created_by = created_by,
            modified_at = modified_at,
            modified_by = modified_by,
            owning_person = owning_person,
            owning_group = owning_group
          )
        )
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return SiteTextTranslationSearchResponse(ErrorType.SQLReadError)
    }
    return SiteTextTranslationSearchResponse(ErrorType.NoError, data)
  }

}
