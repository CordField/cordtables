package com.seedcompany.cordtables.components.tables.sc.ceremonies

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

data class ScCeremoniesReadRequest(
  val token: String?,
  val id: String? = null,
)

data class ScCeremoniesReadResponse(
  val error: ErrorType,
  val ceremony: ceremony? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScCeremoniesRead")
class Read(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("sc/ceremonies/read")
  @ResponseBody
  fun readHandler(@RequestBody req: ScCeremoniesReadRequest): ScCeremoniesReadResponse {

    if (req.token == null) return ScCeremoniesReadResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScCeremoniesReadResponse(ErrorType.AdminOnly)

    if (req.id == null) return ScCeremoniesReadResponse(ErrorType.MissingId)

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)
    paramSource.addValue("id", req.id)

    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "sc.ceremonies",
        getList = false,
        columns = arrayOf(
          "id",
          "internship_engagement",
          "language_engagement",
          "ethnologue",
          "actual_date",
          "estimated_date",
          "is_planned",
          "type",
          "created_at",
          "created_by",
          "modified_at",
          "modified_by",
          "owning_person",
          "owning_group",
        ),
      )
    ).query

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var id: String? = jdbcResult.getString("id")
        if (jdbcResult.wasNull()) id = null

        var internship_engagement: String? = jdbcResult.getString("internship_engagement")
        if (jdbcResult.wasNull()) internship_engagement = null

        var language_engagement: String? = jdbcResult.getString("language_engagement")
        if (jdbcResult.wasNull()) language_engagement = null

        var ethnologue: String? = jdbcResult.getString("ethnologue")
        if (jdbcResult.wasNull()) ethnologue = null

        var actual_date: String? = jdbcResult.getString("actual_date")
        if (jdbcResult.wasNull()) actual_date = null

        var estimated_date: String? = jdbcResult.getString("estimated_date")
        if (jdbcResult.wasNull()) estimated_date = null

        var is_planned: Boolean? = jdbcResult.getBoolean("is_planned")
        if (jdbcResult.wasNull()) is_planned = null

        var type: String? = jdbcResult.getString("type")
        if (jdbcResult.wasNull()) type = null

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

        val ceremony =
          ceremony(
            id = id,
            internship_engagement = internship_engagement,
            language_engagement = language_engagement,
            ethnologue = ethnologue,
            actual_date = actual_date,
            estimated_date = estimated_date,
            is_planned = is_planned,
            type = type,
            created_at = created_at,
            created_by = created_by,
            modified_at = modified_at,
            modified_by = modified_by,
            owning_person = owning_person,
            owning_group = owning_group,
          )

        return ScCeremoniesReadResponse(ErrorType.NoError, ceremony = ceremony)

      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return ScCeremoniesReadResponse(ErrorType.SQLReadError)
    }

    return ScCeremoniesReadResponse(error = ErrorType.UnknownError)
  }
}
