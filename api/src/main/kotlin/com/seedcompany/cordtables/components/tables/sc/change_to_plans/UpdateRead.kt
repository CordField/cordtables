package com.seedcompany.cordtables.components.tables.sc.change_to_plans

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.ScChangeToPlansReadRequest
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.ScChangeToPlansUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.changeToPlan
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.changeToPlanInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScChangeToPlansUpdateReadRequest(
  val token: String?,
  val id: String? = null,
  val column: String? = null,
  val value: Any? = null,
)

data class ScChangeToPlansUpdateReadResponse(
  val error: ErrorType,
  val changeToPlan: changeToPlan? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScChangeToPlansUpdateRead")
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
  @PostMapping("sc/change-to-plans/update-read")
  @ResponseBody
  fun updateReadHandler(@RequestBody req: ScChangeToPlansUpdateReadRequest): ScChangeToPlansUpdateReadResponse {

    if (req.token == null) return ScChangeToPlansUpdateReadResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScChangeToPlansUpdateReadResponse(ErrorType.AdminOnly)

    val updateResponse = update.updateHandler(
      ScChangeToPlansUpdateRequest(
        token = req.token,
        column = req.column,
        id = req.id,
        value = req.value,
      )
    )

    if (updateResponse.error != ErrorType.NoError) {
      return ScChangeToPlansUpdateReadResponse(updateResponse.error)
    }

    val readResponse = read.readHandler(
      ScChangeToPlansReadRequest(
        token = req.token,
        id = req.id!!
      )
    )

    return ScChangeToPlansUpdateReadResponse(error = readResponse.error, readResponse.changeToPlan)
  }
}
