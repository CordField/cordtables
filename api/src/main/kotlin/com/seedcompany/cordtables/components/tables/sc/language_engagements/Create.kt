package com.seedcompany.cordtables.components.tables.sc.language_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.language_engagements.languageEngagementInput
import com.seedcompany.cordtables.components.tables.sc.language_engagements.Read
import com.seedcompany.cordtables.components.tables.sc.language_engagements.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguageEngagementsCreateRequest(
    val token: String? = null,
    val languageEngagement: languageEngagementInput,
)

data class ScLanguageEngagementsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageEngagementsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("sc/language-engagements/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScLanguageEngagementsCreateRequest): ScLanguageEngagementsCreateResponse {

        // if (req.languageEngagement.name == null) return ScLanguageEngagementsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.language_engagements(project, ethnologue, change_to_plan, active, ceremony, is_open_to_investor_visit, communications_complete_date, complete_date, disbursement_complete_date, end_date, 
            end_date_override, initial_end_date, is_first_scripture, is_luke_partnership, is_sent_printing, last_suspended_at, last_reactivated_at, paratext_registry, periodic_reports_directory, pnp, pnp_file, 
            product_engagement_tag, start_date, start_date_override, status, status_modified_at, historic_goal, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?,
                    ?::boolean,
                    ?,
                    ?::boolean,
                    ?::timestamp,
                    ?::timestamp,
                    ?::timestamp,
                    ?::timestamp,
                    ?::timestamp,
                    ?::timestamp,
                    ?::boolean,
                    ?::boolean,
                    ?::boolean,
                    ?::timestamp,
                    ?::timestamp,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?::common.project_engagement_tag,
                    ?::timestamp,
                    ?::timestamp,
                    ?::common.engagement_status,
                    ?::timestamp,
                    ?,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.languageEngagement.project,
            req.languageEngagement.ethnologue,
            req.languageEngagement.change_to_plan,
            req.languageEngagement.active,
            req.languageEngagement.ceremony,
            req.languageEngagement.is_open_to_investor_visit,
            req.languageEngagement.communications_complete_date,
            req.languageEngagement.complete_date,
            req.languageEngagement.disbursement_complete_date,
            req.languageEngagement.end_date,
            req.languageEngagement.end_date_override,
            req.languageEngagement.initial_end_date,
            req.languageEngagement.is_first_scripture,
            req.languageEngagement.is_luke_partnership,
            req.languageEngagement.is_sent_printing,
            req.languageEngagement.last_suspended_at,
            req.languageEngagement.last_reactivated_at,
            req.languageEngagement.paratext_registry,
            req.languageEngagement.periodic_reports_directory,
            req.languageEngagement.pnp,
            req.languageEngagement.pnp_file,
            req.languageEngagement.product_engagement_tag,
            req.languageEngagement.start_date,
            req.languageEngagement.start_date_override,
            req.languageEngagement.status,
            req.languageEngagement.status_modified_at,
            req.languageEngagement.historic_goal,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

        return ScLanguageEngagementsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
