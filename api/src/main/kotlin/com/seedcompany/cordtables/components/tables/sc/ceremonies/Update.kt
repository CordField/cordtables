package com.seedcompany.cordtables.components.tables.sc.ceremonies

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScCeremoniesUpdateRequest(
  val token: String?,
  val id: String? = null,
  val column: String? = null,
  val value: Any? = null,
)

data class ScCeremoniesUpdateResponse(
  val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScCeremoniesUpdate")
class Update(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/ceremonies/update")
  @ResponseBody
  fun updateHandler(@RequestBody req: ScCeremoniesUpdateRequest): ScCeremoniesUpdateResponse {

    if (req.token == null) return ScCeremoniesUpdateResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScCeremoniesUpdateResponse(ErrorType.AdminOnly)
    if (req.column == null) return ScCeremoniesUpdateResponse(ErrorType.InputMissingColumn)
    if (req.id == null) return ScCeremoniesUpdateResponse(ErrorType.MissingId)

    when (req.column) {
      "internship_engagement" -> {
        util.updateField(
          token = req.token,
          table = "sc.ceremonies",
          column = "internship_engagement",
          id = req.id,
          value = req.value
        )
      }
      "language_engagement" -> {
        util.updateField(
          token = req.token,
          table = "sc.ceremonies",
          column = "language_engagement",
          id = req.id,
          value = req.value
        )
      }
      "ethnologue" -> {
        util.updateField(
          token = req.token,
          table = "sc.ceremonies",
          column = "ethnologue",
          id = req.id,
          value = req.value
        )
      }
      "actual_date" -> {
        util.updateField(
          token = req.token,
          table = "sc.ceremonies",
          column = "actual_date",
          id = req.id,
          value = req.value,
          cast = "::timestamp"
        )
      }
      "estimated_date" -> {
        util.updateField(
          token = req.token,
          table = "sc.ceremonies",
          column = "estimated_date",
          id = req.id,
          value = req.value,
          cast = "::timestamp"
        )
      }
      "is_planned" -> {
        util.updateField(
          token = req.token,
          table = "sc.ceremonies",
          column = "is_planned",
          id = req.id,
          value = req.value,
          cast = "::boolean"
        )
      }
      "type" -> {
        util.updateField(
          token = req.token,
          table = "sc.ceremonies",
          column = "type",
          id = req.id,
          value = req.value,
          cast = "::common.ceremony_type"
        )
      }

      "owning_person" -> {
        util.updateField(
          token = req.token,
          table = "sc.ceremonies",
          column = "owning_person",
          id = req.id,
          value = req.value
        )
      }
      "owning_group" -> {
        util.updateField(
          token = req.token,
          table = "sc.ceremonies",
          column = "owning_group",
          id = req.id,
          value = req.value
        )
      }
    }

    return ScCeremoniesUpdateResponse(ErrorType.NoError)
  }

}
