package com.seedcompany.cordtables.components.tables.sc.budgets

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.budgets.ScBudgetsReadRequest
import com.seedcompany.cordtables.components.tables.sc.budgets.ScBudgetsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.budgets.budget
import com.seedcompany.cordtables.components.tables.sc.budgets.budgetInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetsUpdateReadRequest(
  val token: String?,
  val id: String? = null,
  val column: String? = null,
  val value: Any? = null,
)

data class ScBudgetsUpdateReadResponse(
  val error: ErrorType,
  val budget: budget? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetsUpdateRead")
class UpdateRead(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val update: Update,

  @Autowired
  val read: Read,
) {
  @PostMapping("sc/budgets/update-read")
  @ResponseBody
  fun updateReadHandler(@RequestBody req: ScBudgetsUpdateReadRequest): ScBudgetsUpdateReadResponse {

    if (req.token == null) return ScBudgetsUpdateReadResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScBudgetsUpdateReadResponse(ErrorType.AdminOnly)

    val updateResponse = update.updateHandler(
      ScBudgetsUpdateRequest(
        token = req.token,
        column = req.column,
        id = req.id,
        value = req.value,
      )
    )

    if (updateResponse.error != ErrorType.NoError) {
      return ScBudgetsUpdateReadResponse(updateResponse.error)
    }

    val readResponse = read.readHandler(
      ScBudgetsReadRequest(
        token = req.token,
        id = req.id!!
      )
    )

    return ScBudgetsUpdateReadResponse(error = readResponse.error, readResponse.budget)
  }
}
