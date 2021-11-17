package com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.ScGlobalPartnerEngagementPeopleReadRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.ScGlobalPartnerEngagementPeopleUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.globalPartnerEngagementPeople
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.globalPartnerEngagementPeopleInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerEngagementPeopleUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScGlobalPartnerEngagementPeopleUpdateReadResponse(
    val error: ErrorType,
    val globalPartnerEngagementPeople: globalPartnerEngagementPeople? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScGlobalPartnerEngagementPeopleUpdateRead")
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
    @PostMapping("sc-global-partner-engagement-people/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScGlobalPartnerEngagementPeopleUpdateReadRequest): ScGlobalPartnerEngagementPeopleUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScGlobalPartnerEngagementPeopleUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScGlobalPartnerEngagementPeopleUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScGlobalPartnerEngagementPeopleReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScGlobalPartnerEngagementPeopleUpdateReadResponse(error = readResponse.error, readResponse.globalPartnerEngagementPeople)
    }
}