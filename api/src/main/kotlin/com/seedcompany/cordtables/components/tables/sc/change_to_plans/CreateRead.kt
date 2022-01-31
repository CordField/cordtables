package com.seedcompany.cordtables.components.tables.sc.change_to_plans

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.*
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.ScChangeToPlansCreateRequest
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScChangeToPlansCreateReadRequest(
  val token: String? = null,
  val changeToPlan: changeToPlanInput,
)

data class ScChangeToPlansCreateReadResponse(
  val error: ErrorType,
  val changeToPlan: changeToPlan? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScChangeToPlansCreateRead")
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
  @PostMapping("sc/change-to-plans/create-read")
  @ResponseBody
  fun createReadHandler(@RequestBody req: ScChangeToPlansCreateReadRequest): ScChangeToPlansCreateReadResponse {

    if (req.token == null) return ScChangeToPlansCreateReadResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScChangeToPlansCreateReadResponse(ErrorType.AdminOnly)

    val createResponse = create.createHandler(
      ScChangeToPlansCreateRequest(
        token = req.token,
        changeToPlan = req.changeToPlan
      )
    )

    if (createResponse.error != ErrorType.NoError) {
      return ScChangeToPlansCreateReadResponse(error = createResponse.error)
    }

    val readResponse = read.readHandler(
      ScChangeToPlansReadRequest(
        token = req.token,
        id = createResponse!!.id
      )
    )

    return ScChangeToPlansCreateReadResponse(error = readResponse.error, changeToPlan = readResponse.changeToPlan)
  }
}
