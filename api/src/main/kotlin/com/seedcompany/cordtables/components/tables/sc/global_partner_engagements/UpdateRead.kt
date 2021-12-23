package com.seedcompany.cordtables.components.tables.sc.global_partner_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.ScGlobalPartnerEngagementsReadRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.ScGlobalPartnerEngagementsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.globalPartnerEngagement
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.globalPartnerEngagementInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerEngagementsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScGlobalPartnerEngagementsUpdateReadResponse(
    val error: ErrorType,
    val globalPartnerEngagement: globalPartnerEngagement? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerEngagementsUpdateRead")
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
    @PostMapping("sc-global-partner-engagements/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScGlobalPartnerEngagementsUpdateReadRequest): ScGlobalPartnerEngagementsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScGlobalPartnerEngagementsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScGlobalPartnerEngagementsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScGlobalPartnerEngagementsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScGlobalPartnerEngagementsUpdateReadResponse(error = readResponse.error, readResponse.globalPartnerEngagement)
    }
}
