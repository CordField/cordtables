package com.seedcompany.cordtables.components.tables.sc.global_partner_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.*
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.ScGlobalPartnerEngagementsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerEngagementsCreateReadRequest(
    val token: String? = null,
    val globalPartnerEngagement: globalPartnerEngagementInput,
)

data class ScGlobalPartnerEngagementsCreateReadResponse(
    val error: ErrorType,
    val globalPartnerEngagement: globalPartnerEngagement? = null,
)


@Controller("ScGlobalPartnerEngagementsCreateRead")
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
    @PostMapping("sc-global-partner-engagements/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScGlobalPartnerEngagementsCreateReadRequest): ScGlobalPartnerEngagementsCreateReadResponse {

        val createResponse = create.createHandler(
            ScGlobalPartnerEngagementsCreateRequest(
                token = req.token,
                globalPartnerEngagement = req.globalPartnerEngagement
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScGlobalPartnerEngagementsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScGlobalPartnerEngagementsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScGlobalPartnerEngagementsCreateReadResponse(error = readResponse.error, globalPartnerEngagement = readResponse.globalPartnerEngagement)
    }
}