package com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.*
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.ScGlobalPartnerEngagementPeopleCreateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerEngagementPeopleCreateReadRequest(
    val token: String? = null,
    val globalPartnerEngagementPeople: globalPartnerEngagementPeopleInput,
)

data class ScGlobalPartnerEngagementPeopleCreateReadResponse(
    val error: ErrorType,
    val globalPartnerEngagementPeople: globalPartnerEngagementPeople? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScGlobalPartnerEngagementPeopleCreateRead")
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
    @PostMapping("sc-global-partner-engagement-people/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScGlobalPartnerEngagementPeopleCreateReadRequest): ScGlobalPartnerEngagementPeopleCreateReadResponse {

        val createResponse = create.createHandler(
            ScGlobalPartnerEngagementPeopleCreateRequest(
                token = req.token,
                globalPartnerEngagementPeople = req.globalPartnerEngagementPeople
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScGlobalPartnerEngagementPeopleCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScGlobalPartnerEngagementPeopleReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScGlobalPartnerEngagementPeopleCreateReadResponse(error = readResponse.error, globalPartnerEngagementPeople = readResponse.globalPartnerEngagementPeople)
    }
}