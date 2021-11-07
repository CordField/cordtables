package com.seedcompany.cordtables.components.tables.sc.global_partner_engagements

import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.ScGlobalPartnerEngagementsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.file_versions.CommonFileVersionsUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.globalPartnerEngagementInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerEngagementsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScGlobalPartnerEngagementsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScGlobalPartnerEngagementsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-global-partner-engagements/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScGlobalPartnerEngagementsUpdateRequest): ScGlobalPartnerEngagementsUpdateResponse {

        if (req.token == null) return ScGlobalPartnerEngagementsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScGlobalPartnerEngagementsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScGlobalPartnerEngagementsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "organization" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagements",
                    column = "organization",
                    id = req.id,
                    value = req.value,
                )
            }
            "type" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagements",
                    column = "type",
                    id = req.id,
                    value = req.value,
                )
            }
            "mou_start" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagements",
                    column = "mou_start",
                    id = req.id,
                    value = req.value,
                )
            }
            "mou_end" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagements",
                    column = "mou_end",
                    id = req.id,
                    value = req.value,
                )
            }
            "sc_roles" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagements",
                    column = "sc_roles",
                    id = req.id,
                    value = req.value,
                )
            }
            "partner_roles" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagements",
                    column = "partner_roles",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagements",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_engagements",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return ScGlobalPartnerEngagementsUpdateResponse(ErrorType.NoError)
    }

}