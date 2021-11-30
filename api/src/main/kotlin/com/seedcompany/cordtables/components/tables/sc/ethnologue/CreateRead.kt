package com.seedcompany.cordtables.components.tables.sc.ethnologue

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.ethnologue.*
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ScEthnologueCreateRequest
import com.seedcompany.cordtables.components.tables.sc.ethnologue.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScEthnologueCreateReadRequest(
    val token: String? = null,
    val ethnologue: ethnologueInput,
)

data class ScEthnologueCreateReadResponse(
    val error: ErrorType,
    val ethnologue: ethnologue? = null,
)


@Controller("ScEthnologueCreateRead")
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
    @PostMapping("sc-ethnologue/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScEthnologueCreateReadRequest): ScEthnologueCreateReadResponse {

        val createResponse = create.createHandler(
            ScEthnologueCreateRequest(
                token = req.token,
                ethnologue = req.ethnologue
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScEthnologueCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScEthnologueReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScEthnologueCreateReadResponse(error = readResponse.error, ethnologue = readResponse.ethnologue)
    }
}