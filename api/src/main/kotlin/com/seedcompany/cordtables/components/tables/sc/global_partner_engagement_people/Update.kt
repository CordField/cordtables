package com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerEngagementPeopleUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScGlobalPartnerEngagementPeopleUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerEngagementPeopleUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/global-partner-engagement-people/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScGlobalPartnerEngagementPeopleUpdateRequest): ScGlobalPartnerEngagementPeopleUpdateResponse {

        if (req.token == null) return ScGlobalPartnerEngagementPeopleUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScGlobalPartnerEngagementPeopleUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScGlobalPartnerEngagementPeopleUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "engagement" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagement_people",
                    column = "engagement",
                    id = req.id,
                    value = req.value
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagement_people",
                    column = "person",
                    id = req.id,
                    value = req.value
                )
            }
            "role" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagement_people",
                    column = "role",
                    id = req.id,
                    value = req.value,
                    cast = "::common.people_to_org_relationship_type"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagement_people",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagement_people",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return ScGlobalPartnerEngagementPeopleUpdateResponse(ErrorType.NoError)
    }

}
