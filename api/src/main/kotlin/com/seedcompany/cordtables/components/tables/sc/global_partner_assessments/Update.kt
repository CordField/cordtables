package com.seedcompany.cordtables.components.tables.sc.global_partner_assessments

import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.ScGlobalPartnerAssessmentsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.globalPartnerAssessmentInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerAssessmentsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScGlobalPartnerAssessmentsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScGlobalPartnerAssessmentsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-global-partner-assessments/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScGlobalPartnerAssessmentsUpdateRequest): ScGlobalPartnerAssessmentsUpdateResponse {

        if (req.token == null) return ScGlobalPartnerAssessmentsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScGlobalPartnerAssessmentsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScGlobalPartnerAssessmentsUpdateResponse(ErrorType.MissingId)

        when (req.column) {

            "partner" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "partner",
                    id = req.id,
                    value = req.value,
                )
            }
            "governance_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "governance_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "director_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "director_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "identity_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "identity_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "growth_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "growth_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "comm_support_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "comm_support_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "systems_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "systems_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "fin_management_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "fin_management_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "hr_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "hr_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "it_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "it_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "program_design_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "program_design_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "tech_translation_trans" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "tech_translation_trans",
                    id = req.id,
                    value = req.value,
                )
            }
            "director_opp" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "director_opp",
                    id = req.id,
                    value = req.value,
                )
            }
            "financial_management_opp" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "financial_management_opp",
                    id = req.id,
                    value = req.value,
                )
            }
            "program_design_opp" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "program_design_opp",
                    id = req.id,
                    value = req.value,
                )
            }
            "director" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "director",
                    id = req.id,
                    value = req.value,
                )
            }
            "tech_translation_opp" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "tech_translation_opp",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_assessments",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return ScGlobalPartnerAssessmentsUpdateResponse(ErrorType.NoError)
    }

}