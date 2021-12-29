package com.seedcompany.cordtables.components.tables.sc.ethnologue

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ScEthnologueReadRequest
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ScEthnologueUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ethnologue
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ethnologueInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScEthnologueUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScEthnologueUpdateReadResponse(
    val error: ErrorType,
    val ethnologue: ethnologue? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScEthnologueUpdateRead")
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
    @PostMapping("sc/ethnologue/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScEthnologueUpdateReadRequest): ScEthnologueUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScEthnologueUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScEthnologueUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScEthnologueReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScEthnologueUpdateReadResponse(error = readResponse.error, readResponse.ethnologue)
    }
}
