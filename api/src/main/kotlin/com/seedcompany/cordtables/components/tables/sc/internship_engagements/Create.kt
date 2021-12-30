package com.seedcompany.cordtables.components.tables.sc.internship_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.internshipEngagementInput
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.Read
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScInternshipEngagementsCreateRequest(
    val token: String? = null,
    val internshipEngagement: internshipEngagementInput,
)

data class ScInternshipEngagementsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScInternshipEngagementsCreate")
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

    @PostMapping("sc/internship-engagements/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScInternshipEngagementsCreateRequest): ScInternshipEngagementsCreateResponse {

        // if (req.internshipEngagement.name == null) return ScInternshipEngagementsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.internship_engagements(project, change_to_plan, active, ceremony, communications_complete_date, complete_date,
             country_of_origin, disbursement_complete_date, end_date, end_date_override, growth_plan, initial_end_date, intern, last_reactivated_at,
             mentor, methodologies, paratext_registry, periodic_reports_directory, position, sensitivity, start_date, start_date_override, status, 
             status_modified_at, last_suspended_at, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
                    ?::boolean,
                    ?::uuid,
                    ?::timestamp,
                    ?::timestamp,
                    ?::uuid,
                    ?::timestamp,
                    ?::timestamp,
                    ?::timestamp,
                    ?::uuid,
                    ?::timestamp,
                    ?::uuid,
                    ?::timestamp,
                    ?::uuid,
                    ARRAY[?]::common.product_methodologies[],
                    ?,
                    ?::uuid,
                    ?::common.internship_position,
                    ?::common.sensitivity,
                    ?::timestamp,
                    ?::timestamp,
                    ?::common.engagement_status,
                    ?::timestamp,
                    ?::timestamp,
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
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.internshipEngagement.project,
            req.internshipEngagement.change_to_plan,
            req.internshipEngagement.active,
            req.internshipEngagement.ceremony,
            req.internshipEngagement.communications_complete_date,
            req.internshipEngagement.complete_date,
            req.internshipEngagement.country_of_origin,
            req.internshipEngagement.disbursement_complete_date,
            req.internshipEngagement.end_date,
            req.internshipEngagement.end_date_override,
            req.internshipEngagement.growth_plan,
            req.internshipEngagement.initial_end_date,
            req.internshipEngagement.intern,
            req.internshipEngagement.last_reactivated_at,
            req.internshipEngagement.mentor,
            req.internshipEngagement.methodologies,
            req.internshipEngagement.paratext_registry,
            req.internshipEngagement.periodic_reports_directory,
            req.internshipEngagement.position,
            req.internshipEngagement.sensitivity,
            req.internshipEngagement.start_date,
            req.internshipEngagement.start_date_override,
            req.internshipEngagement.status,
            req.internshipEngagement.status_modified_at,
            req.internshipEngagement.last_suspended_at,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return ScInternshipEngagementsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
