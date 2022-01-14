package com.seedcompany.cordtables.components.tables.sc.budgets

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetsUpdateRequest(
  val token: String?,
  val id: String? = null,
  val column: String? = null,
  val value: Any? = null,
)

data class ScBudgetsUpdateResponse(
  val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetsUpdate")
class Update(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/budgets/update")
  @ResponseBody
  fun updateHandler(@RequestBody req: ScBudgetsUpdateRequest): ScBudgetsUpdateResponse {

    if (req.token == null) return ScBudgetsUpdateResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScBudgetsUpdateResponse(ErrorType.AdminOnly)
    if (req.column == null) return ScBudgetsUpdateResponse(ErrorType.InputMissingColumn)
    if (req.id == null) return ScBudgetsUpdateResponse(ErrorType.MissingId)

    when (req.column) {
      "change_to_plan" -> {
        util.updateField(
          token = req.token,
          table = "sc.budgets",
          column = "change_to_plan",
          id = req.id,
          value = req.value,
          cast = "::uuid"
        )
      }
      "project" -> {
        util.updateField(
          token = req.token,
          table = "sc.budgets",
          column = "project",
          id = req.id,
          value = req.value,
          cast = "::uuid"
        )
      }

      "status" -> {
        util.updateField(
          token = req.token,
          table = "sc.budgets",
          column = "status",
          id = req.id,
          value = req.value,
          cast = "::common.budget_status"
        )
      }
      "universal_template" -> {
        util.updateField(
          token = req.token,
          table = "sc.budgets",
          column = "universal_template",
          id = req.id,
          value = req.value,
          cast = "::uuid"
        )
      }
      "universal_template_file_url" -> {
        util.updateField(
          token = req.token,
          table = "sc.budgets",
          column = "universal_template_file_url",
          id = req.id,
          value = req.value,
        )
      }
      "sensitivity" -> {
        util.updateField(
          token = req.token,
          table = "sc.budgets",
          column = "sensitivity",
          id = req.id,
          value = req.value,
          cast = "::common.sensitivity"
        )
      }
      "total" -> {
        util.updateField(
          token = req.token,
          table = "sc.budgets",
          column = "total",
          id = req.id,
          value = req.value,
          cast = "::decimal"
        )
      }

      "owning_person" -> {
        util.updateField(
          token = req.token,
          table = "sc.budgets",
          column = "owning_person",
          id = req.id,
          value = req.value,
          cast = "::uuid"
        )
      }
      "owning_group" -> {
        util.updateField(
          token = req.token,
          table = "sc.budgets",
          column = "owning_group",
          id = req.id,
          value = req.value,
          cast = "::uuid"
        )
      }
    }

    return ScBudgetsUpdateResponse(ErrorType.NoError)
  }

}
