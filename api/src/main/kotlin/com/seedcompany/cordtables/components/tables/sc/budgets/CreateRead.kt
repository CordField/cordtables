package com.seedcompany.cordtables.components.tables.sc.budgets

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.budgets.*
import com.seedcompany.cordtables.components.tables.sc.budgets.ScBudgetsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.budgets.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetsCreateReadRequest(
  val token: String? = null,
  val budget: budgetInput,
)

data class ScBudgetsCreateReadResponse(
  val error: ErrorType,
  val budget: budget? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetsCreateRead")
class CreateRead(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val create: Create,

  @Autowired
  val read: Read,
) {
  @PostMapping("sc/budgets/create-read")
  @ResponseBody
  fun createReadHandler(@RequestBody req: ScBudgetsCreateReadRequest): ScBudgetsCreateReadResponse {

    if (req.token == null) return ScBudgetsCreateReadResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScBudgetsCreateReadResponse(ErrorType.AdminOnly)

    val createResponse = create.createHandler(
      ScBudgetsCreateRequest(
        token = req.token,
        budget = req.budget
      )
    )

    if (createResponse.error != ErrorType.NoError) {
      return ScBudgetsCreateReadResponse(error = createResponse.error)
    }

    val readResponse = read.readHandler(
      ScBudgetsReadRequest(
        token = req.token,
        id = createResponse!!.id
      )
    )

    return ScBudgetsCreateReadResponse(error = readResponse.error, budget = readResponse.budget)
  }
}
