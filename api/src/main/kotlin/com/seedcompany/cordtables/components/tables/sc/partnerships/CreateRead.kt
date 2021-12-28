package com.seedcompany.cordtables.components.tables.sc.partnerships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.partnerships.*
import com.seedcompany.cordtables.components.tables.sc.partnerships.ScPartnershipsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.partnerships.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPartnershipsCreateReadRequest(
    val token: String? = null,
    val partnership: partnershipInput,
)

data class ScPartnershipsCreateReadResponse(
    val error: ErrorType,
    val partnership: partnership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPartnershipsCreateRead")
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
    @PostMapping("sc/partnerships/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScPartnershipsCreateReadRequest): ScPartnershipsCreateReadResponse {

        val createResponse = create.createHandler(
            ScPartnershipsCreateRequest(
                token = req.token,
                partnership = req.partnership
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScPartnershipsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScPartnershipsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScPartnershipsCreateReadResponse(error = readResponse.error, partnership = readResponse.partnership)
    }
}
