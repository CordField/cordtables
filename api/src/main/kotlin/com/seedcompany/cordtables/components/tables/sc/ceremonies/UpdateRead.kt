package com.seedcompany.cordtables.components.tables.sc.ceremonies

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.ceremonies.ScCeremoniesReadRequest
import com.seedcompany.cordtables.components.tables.sc.ceremonies.ScCeremoniesUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.ceremonies.ceremony
import com.seedcompany.cordtables.components.tables.sc.ceremonies.ceremonyInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScCeremoniesUpdateReadRequest(
  val token: String?,
  val id: String? = null,
  val column: String? = null,
  val value: Any? = null,
)

data class ScCeremoniesUpdateReadResponse(
  val error: ErrorType,
  val ceremony: ceremony? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScCeremoniesUpdateRead")
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
  @PostMapping("sc/ceremonies/update-read")
  @ResponseBody
  fun updateReadHandler(@RequestBody req: ScCeremoniesUpdateReadRequest): ScCeremoniesUpdateReadResponse {

    if (req.token == null) return ScCeremoniesUpdateReadResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScCeremoniesUpdateReadResponse(ErrorType.AdminOnly)

    val updateResponse = update.updateHandler(
      ScCeremoniesUpdateRequest(
        token = req.token,
        column = req.column,
        id = req.id,
        value = req.value,
      )
    )

    if (updateResponse.error != ErrorType.NoError) {
      return ScCeremoniesUpdateReadResponse(updateResponse.error)
    }

    val readResponse = read.readHandler(
      ScCeremoniesReadRequest(
        token = req.token,
        id = req.id!!
      )
    )

    return ScCeremoniesUpdateReadResponse(error = readResponse.error, readResponse.ceremony)
  }
}
