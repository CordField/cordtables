package com.seedcompany.cordtables.components.tables.sc.change_to_plans

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScChangeToPlansUpdateRequest(
  val token: String?,
  val id: String? = null,
  val column: String? = null,
  val value: Any? = null,
)

data class ScChangeToPlansUpdateResponse(
  val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScChangeToPlansUpdate")
class Update(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/change-to-plans/update")
  @ResponseBody
  fun updateHandler(@RequestBody req: ScChangeToPlansUpdateRequest): ScChangeToPlansUpdateResponse {

    if (req.token == null) return ScChangeToPlansUpdateResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScChangeToPlansUpdateResponse(ErrorType.AdminOnly)
    if (req.column == null) return ScChangeToPlansUpdateResponse(ErrorType.InputMissingColumn)
    if (req.id == null) return ScChangeToPlansUpdateResponse(ErrorType.MissingId)

    when (req.column) {
      "status" -> {
        util.updateField(
          token = req.token,
          table = "sc.change_to_plans",
          column = "status",
          id = req.id,
          value = req.value,
          cast = "::sc.change_to_plan_status"
        )
      }
      "summary" -> {
        util.updateField(
          token = req.token,
          table = "sc.change_to_plans",
          column = "summary",
          id = req.id,
          value = req.value
        )
      }
      "type" -> {
        util.updateField(
          token = req.token,
          table = "sc.change_to_plans",
          column = "type",
          id = req.id,
          value = req.value,
          cast = "::sc.change_to_plan_type"
        )
      }
      "owning_person" -> {
        util.updateField(
          token = req.token,
          table = "sc.change_to_plans",
          column = "owning_person",
          id = req.id,
          value = req.value,
          cast = "::uuid"
        )
      }
      "owning_group" -> {
        util.updateField(
          token = req.token,
          table = "sc.change_to_plans",
          column = "owning_group",
          id = req.id,
          value = req.value,
          cast = "::uuid"
        )
      }
    }

    return ScChangeToPlansUpdateResponse(ErrorType.NoError)
  }

}
