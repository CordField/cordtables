package com.seedcompany.cordtables.components.tables.sc.change_to_plans

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

data class ScChangeToPlansReadRequest(
  val token: String?,
  val id: String? = null,
)

data class ScChangeToPlansReadResponse(
  val error: ErrorType,
  val changeToPlan: changeToPlan? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScChangeToPlansRead")
class Read(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("sc/change-to-plans/read")
  @ResponseBody
  fun readHandler(@RequestBody req: ScChangeToPlansReadRequest): ScChangeToPlansReadResponse {

    if (req.token == null) return ScChangeToPlansReadResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScChangeToPlansReadResponse(ErrorType.AdminOnly)

    if (req.id == null) return ScChangeToPlansReadResponse(ErrorType.MissingId)

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)
    paramSource.addValue("id", req.id)

    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "sc.change_to_plans",
        getList = false,
        columns = arrayOf(
          "id",
          "status",
          "summary",
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

        var status: String? = jdbcResult.getString("status")
        if (jdbcResult.wasNull()) status = null

        var summary: String? = jdbcResult.getString("summary")
        if (jdbcResult.wasNull()) summary = null

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

        val changeToPlan =
          changeToPlan(
            id = id,
            status = status,
            summary = summary,
            type = type,
            created_at = created_at,
            created_by = created_by,
            modified_at = modified_at,
            modified_by = modified_by,
            owning_person = owning_person,
            owning_group = owning_group,
          )

        return ScChangeToPlansReadResponse(ErrorType.NoError, changeToPlan = changeToPlan)

      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return ScChangeToPlansReadResponse(ErrorType.SQLReadError)
    }

    return ScChangeToPlansReadResponse(error = ErrorType.UnknownError)
  }
}
