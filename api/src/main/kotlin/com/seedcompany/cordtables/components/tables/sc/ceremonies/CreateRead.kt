package com.seedcompany.cordtables.components.tables.sc.ceremonies

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.ceremonies.*
import com.seedcompany.cordtables.components.tables.sc.ceremonies.ScCeremoniesCreateRequest
import com.seedcompany.cordtables.components.tables.sc.ceremonies.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScCeremoniesCreateReadRequest(
  val token: String? = null,
  val ceremony: ceremonyInput,
)

data class ScCeremoniesCreateReadResponse(
  val error: ErrorType,
  val ceremony: ceremony? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScCeremoniesCreateRead")
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
  @PostMapping("sc/ceremonies/create-read")
  @ResponseBody
  fun createReadHandler(@RequestBody req: ScCeremoniesCreateReadRequest): ScCeremoniesCreateReadResponse {

    if (req.token == null) return ScCeremoniesCreateReadResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScCeremoniesCreateReadResponse(ErrorType.AdminOnly)

    val createResponse = create.createHandler(
      ScCeremoniesCreateRequest(
        token = req.token,
        ceremony = req.ceremony
      )
    )

    if (createResponse.error != ErrorType.NoError) {
      return ScCeremoniesCreateReadResponse(error = createResponse.error)
    }

    val readResponse = read.readHandler(
      ScCeremoniesReadRequest(
        token = req.token,
        id = createResponse!!.id
      )
    )

    return ScCeremoniesCreateReadResponse(error = readResponse.error, ceremony = readResponse.ceremony)
  }
}
