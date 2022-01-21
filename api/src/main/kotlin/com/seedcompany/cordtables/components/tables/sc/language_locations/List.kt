package com.seedcompany.cordtables.components.tables.sc.language_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
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


data class ScLanguageLocationsListRequest(
  val token: String?
)

data class ScLanguageLocationsListResponse(
  val error: ErrorType,
  val languageLocations: MutableList<languageLocation>? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageLocationsList")
class List(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {

  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("sc/language-locations/list")
  @ResponseBody
  fun listHandler(@RequestBody req: ScLanguageLocationsListRequest): ScLanguageLocationsListResponse {

    if (req.token == null) return ScLanguageLocationsListResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScLanguageLocationsListResponse(ErrorType.AdminOnly)

    var data: MutableList<languageLocation> = mutableListOf()
    if (req.token == null) return ScLanguageLocationsListResponse(ErrorType.TokenNotFound, mutableListOf())

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)

    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "sc.language_locations",
        filter = "order by id",
        columns = arrayOf(
          "id",
          "language",
          "location",
          "created_at",
          "created_by",
          "modified_at",
          "modified_by",
          "owning_person",
          "owning_group",
        )
      )
    ).query

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var id: String? = jdbcResult.getString("id")
        if (jdbcResult.wasNull()) id = null

        var language: String? = jdbcResult.getString("language")
        if (jdbcResult.wasNull()) language = null

        var location: String? = jdbcResult.getString("location")
        if (jdbcResult.wasNull()) location = null

        var created_by: String? = jdbcResult.getString("created_by")
        if (jdbcResult.wasNull()) created_by = null

        var created_at: String? = jdbcResult.getString("created_at")
        if (jdbcResult.wasNull()) created_at = null

        var modified_at: String? = jdbcResult.getString("modified_at")
        if (jdbcResult.wasNull()) modified_at = null

        var modified_by: String? = jdbcResult.getString("modified_by")
        if (jdbcResult.wasNull()) modified_by = null

        var owning_person: String? = jdbcResult.getString("owning_person")
        if (jdbcResult.wasNull()) owning_person = null

        var owning_group: String? = jdbcResult.getString("owning_group")
        if (jdbcResult.wasNull()) owning_group = null

        data.add(
          languageLocation(
            id = id,
            language = language,
            location = location,
            created_at = created_at,
            created_by = created_by,
            modified_at = modified_at,
            modified_by = modified_by,
            owning_person = owning_person,
            owning_group = owning_group,
          )
        )
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return ScLanguageLocationsListResponse(ErrorType.SQLReadError, mutableListOf())
    }

    return ScLanguageLocationsListResponse(ErrorType.NoError, data)
  }
}

