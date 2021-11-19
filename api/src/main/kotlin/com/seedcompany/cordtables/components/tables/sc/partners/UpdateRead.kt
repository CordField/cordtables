package com.seedcompany.cordtables.components.tables.sc.partners

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.partners.ScPartnersReadRequest
import com.seedcompany.cordtables.components.tables.sc.partners.ScPartnersUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.partners.partner
import com.seedcompany.cordtables.components.tables.sc.partners.partnerInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPartnersUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPartnersUpdateReadResponse(
    val error: ErrorType,
    val partner: partner? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScPartnersUpdateRead")
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
    @PostMapping("sc-partners/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScPartnersUpdateReadRequest): ScPartnersUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScPartnersUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScPartnersUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScPartnersReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScPartnersUpdateReadResponse(error = readResponse.error, readResponse.partner)
    }
}