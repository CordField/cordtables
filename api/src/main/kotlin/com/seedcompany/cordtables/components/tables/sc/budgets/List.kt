package com.seedcompany.cordtables.components.tables.sc.budgets

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
import java.math.BigDecimal
import java.sql.SQLException
import javax.sql.DataSource


data class ScBudgetsListRequest(
  val token: String?
)

data class ScBudgetsListResponse(
  val error: ErrorType,
  val budgets: MutableList<budget>? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetsList")
class List(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {

  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("sc/budgets/list")
  @ResponseBody
  fun listHandler(@RequestBody req: ScBudgetsListRequest): ScBudgetsListResponse {

    if (req.token == null) return ScBudgetsListResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScBudgetsListResponse(ErrorType.AdminOnly)

    var data: MutableList<budget> = mutableListOf()
    if (req.token == null) return ScBudgetsListResponse(ErrorType.TokenNotFound, mutableListOf())

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)

    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "sc.budgets",
        filter = "order by id",
        columns = arrayOf(
          "id",
          "change_to_plan",
          "project",
          "status",
          "universal_template",
          "universal_template_file_url",
          "sensitivity",
          "total",
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

        var change_to_plan: String? = jdbcResult.getString("change_to_plan")
        if (jdbcResult.wasNull()) change_to_plan = null

        var project: String? = jdbcResult.getString("project")
        if (jdbcResult.wasNull()) project = null

        var status: String? = jdbcResult.getString("status")
        if (jdbcResult.wasNull()) status = null

        var universal_template: String? = jdbcResult.getString("universal_template")
        if (jdbcResult.wasNull()) universal_template = null

        var universal_template_file_url: String? = jdbcResult.getString("universal_template_file_url")
        if (jdbcResult.wasNull()) universal_template_file_url = null

        var sensitivity: String? = jdbcResult.getString("sensitivity")
        if (jdbcResult.wasNull()) sensitivity = null

        var total: String? = jdbcResult.getString("total")
        if (jdbcResult.wasNull()) total = null

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
          budget(
            id = id,
            change_to_plan = change_to_plan,
            project = project,
            status = status,
            universal_template = universal_template,
            universal_template_file_url = universal_template_file_url,
            sensitivity = sensitivity,
            total = total,
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
      return ScBudgetsListResponse(ErrorType.SQLReadError, mutableListOf())
    }

    return ScBudgetsListResponse(ErrorType.NoError, data)
  }
}

