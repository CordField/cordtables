package com.seedcompany.cordtables.components.tables.sc.periodic_reports

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


data class ScPeriodicReportsListRequest(
  val token: String?
)

data class ScPeriodicReportsListResponse(
  val error: ErrorType,
  val periodicReports: MutableList<periodicReport>? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPeriodicReportsList")
class List(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {

  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("sc/periodic-reports/list")
  @ResponseBody
  fun listHandler(@RequestBody req: ScPeriodicReportsListRequest): ScPeriodicReportsListResponse {

    if (req.token == null) return ScPeriodicReportsListResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScPeriodicReportsListResponse(ErrorType.AdminOnly)

    var data: MutableList<periodicReport> = mutableListOf()
    if (req.token == null) return ScPeriodicReportsListResponse(ErrorType.TokenNotFound, mutableListOf())

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)

    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "sc.periodic_reports",
        filter = "order by id",
        columns = arrayOf(
          "id",
          "directory",
          "end_at",
          "report_file",
          "start_at",
          "type",
          "skipped_reason",
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

        var directory: String? = jdbcResult.getString("directory")
        if (jdbcResult.wasNull()) directory = null

        var end_at: String? = jdbcResult.getString("end_at")
        if (jdbcResult.wasNull()) end_at = null

        var report_file: String? = jdbcResult.getString("report_file")
        if (jdbcResult.wasNull()) report_file = null

        var start_at: String? = jdbcResult.getString("start_at")
        if (jdbcResult.wasNull()) start_at = null

        var type: String? = jdbcResult.getString("type")
        if (jdbcResult.wasNull()) type = null

        var skipped_reason: String? = jdbcResult.getString("skipped_reason")
        if (jdbcResult.wasNull()) skipped_reason = null

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
          periodicReport(
            id = id,
            directory = directory,
            end_at = end_at,
            report_file = report_file,
            start_at = start_at,
            type = type,
            skipped_reason = skipped_reason,
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
      return ScPeriodicReportsListResponse(ErrorType.SQLReadError, mutableListOf())
    }

    return ScPeriodicReportsListResponse(ErrorType.NoError, data)
  }
}

