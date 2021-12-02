package com.seedcompany.cordtables.components.tables.sc.language_engagements

import com.seedcompany.cordtables.components.tables.sc.language_engagements.ScLanguageEngagementsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.language_engagements.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.language_engagements.ScLanguageEngagementsUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.language_engagements.languageEngagementInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguageEngagementsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScLanguageEngagementsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageEngagementsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-language-engagements/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScLanguageEngagementsUpdateRequest): ScLanguageEngagementsUpdateResponse {

        if (req.token == null) return ScLanguageEngagementsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScLanguageEngagementsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScLanguageEngagementsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "neo4j_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "neo4j_id",
                    id = req.id,
                    value = req.value,
                )
            }
            "project" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "project",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "ethnologue" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "ethnologue",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "change_to_plan" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "change_to_plan",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "active" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "active",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "communications_complete_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "communications_complete_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "complete_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "complete_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "disbursement_complete_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "disbursement_complete_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "end_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "end_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "end_date_override" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "end_date_override",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "initial_end_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "initial_end_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "is_first_scripture" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "is_first_scripture",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "is_luke_partnership" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "is_luke_partnership",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "is_sent_printing" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "is_sent_printing",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "last_reactivated_at" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "last_reactivated_at",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "paratext_registry" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "paratext_registry",
                    id = req.id,
                    value = req.value,
                )
            }

            "periodic_reports_directory" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "periodic_reports_directory",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "pnp" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "pnp",
                    id = req.id,
                    value = req.value,
                )
            }
            "pnp_file" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "pnp_file",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "product_engagement_tag" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "product_engagement_tag",
                    id = req.id,
                    value = req.value,
                    cast = "::common.project_engagement_tag"
                )
            }
            "start_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "start_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "start_date_override" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "start_date_override",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "status" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "status",
                    id = req.id,
                    value = req.value,
                    cast = "::common.engagement_status"
                )
            }



            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_engagements",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScLanguageEngagementsUpdateResponse(ErrorType.NoError)
    }

}

