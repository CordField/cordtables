package com.seedcompany.cordtables.components.tables.sc.partners

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.partners.*
import com.seedcompany.cordtables.components.tables.sc.partners.ScPartnersCreateRequest
import com.seedcompany.cordtables.components.tables.sc.partners.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPartnersCreateReadRequest(
    val token: String? = null,
    val partner: partnerInput,
)

data class ScPartnersCreateReadResponse(
    val error: ErrorType,
    val partner: partner? = null,
)


@Controller("ScPartnersCreateRead")
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
    @PostMapping("sc-partners/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScPartnersCreateReadRequest): ScPartnersCreateReadResponse {

        val createResponse = create.createHandler(
            ScPartnersCreateRequest(
                token = req.token,
                partner = req.partner
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScPartnersCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScPartnersReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScPartnersCreateReadResponse(error = readResponse.error, partner = readResponse.partner)
    }
}