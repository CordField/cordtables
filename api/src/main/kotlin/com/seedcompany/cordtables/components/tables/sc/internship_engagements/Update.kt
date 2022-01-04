package com.seedcompany.cordtables.components.tables.sc.internship_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScInternshipEngagementsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScInternshipEngagementsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScInternshipEngagementsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/internship-engagements/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScInternshipEngagementsUpdateRequest): ScInternshipEngagementsUpdateResponse {

        if (req.token == null) return ScInternshipEngagementsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScInternshipEngagementsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScInternshipEngagementsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "project" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "project",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "change_to_plan" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "change_to_plan",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "active" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "active",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "ceremony" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "ceremony",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "communications_complete_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "communications_complete_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "complete_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "complete_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "country_of_origin" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "country_of_origin",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "disbursement_complete_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "disbursement_complete_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "end_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "end_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "end_date_override" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "end_date_override",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "growth_plan" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "growth_plan",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "initial_end_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "initial_end_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "intern" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "intern",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "last_reactivated_at" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "last_reactivated_at",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "mentor" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "mentor",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "methodologies" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "methodologies",
                    id = req.id,
                    value = req.value,
                    cast = "::common.product_methodologies"
                )
            }
            "paratext_registry" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "paratext_registry",
                    id = req.id,
                    value = req.value,
                )
            }
            "periodic_reports_directory" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "periodic_reports_directory",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "position" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "position",
                    id = req.id,
                    value = req.value,
                    cast = "::common.internship_position"
                )
            }
            "sensitivity" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "sensitivity",
                    id = req.id,
                    value = req.value,
                    cast = "::common.sensitivity"
                )
            }
            "start_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "start_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "start_date_override" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "start_date_override",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "status" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "status",
                    id = req.id,
                    value = req.value,
                    cast = "::common.engagement_status"
                )
            }
            "status_modified_at" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "status_modified_at",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "status_modified_at" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "status_modified_at",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.internship_engagements",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return ScInternshipEngagementsUpdateResponse(ErrorType.NoError)
    }

}
