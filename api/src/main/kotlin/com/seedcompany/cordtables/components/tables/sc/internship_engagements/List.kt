package com.seedcompany.cordtables.components.tables.sc.internship_engagements

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.internshipEngagement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class ScInternshipEngagementsListRequest(
    val token: String?
)

data class ScInternshipEngagementsListResponse(
    val error: ErrorType,
    val internshipEngagements: MutableList<internshipEngagement>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScInternshipEngagementsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/internship-engagements/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScInternshipEngagementsListRequest): ScInternshipEngagementsListResponse {
        var data: MutableList<internshipEngagement> = mutableListOf()
        if (req.token == null) return ScInternshipEngagementsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.internship_engagements",
                filter = "order by id",
                columns = arrayOf(
                    "id",

                    "project",
                    "change_to_plan",
                    "active",
                    "ceremony",
                    "communications_complete_date",
                    "complete_date",
                    "country_of_origin",
                    "disbursement_complete_date",
                    "end_date",
                    "end_date_override",
                    "growth_plan",
                    "initial_end_date",
                    "intern",
                    "last_reactivated_at",
                    "mentor",
                    "methodologies",
                    "paratext_registry",
                    "periodic_reports_directory",
                    "position",
                    "sensitivity",
                    "start_date",
                    "start_date_override",
                    "status",
                    "status_modified_at",
                    "last_suspended_at",

                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null




                var project: String? = jdbcResult.getString("project")
                if (jdbcResult.wasNull()) project = null

                var change_to_plan: String? = jdbcResult.getString("change_to_plan")
                if (jdbcResult.wasNull()) change_to_plan = null

                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null

                var ceremony: String? = jdbcResult.getString("ceremony")
                if (jdbcResult.wasNull()) ceremony = null

                var communications_complete_date: String? = jdbcResult.getString("communications_complete_date")
                if (jdbcResult.wasNull()) communications_complete_date = null

                var complete_date: String? = jdbcResult.getString("complete_date")
                if (jdbcResult.wasNull()) complete_date = null

                var country_of_origin: String? = jdbcResult.getString("country_of_origin")
                if (jdbcResult.wasNull()) country_of_origin = null

                var disbursement_complete_date: String? = jdbcResult.getString("disbursement_complete_date")
                if (jdbcResult.wasNull()) disbursement_complete_date = null

                var end_date: String? = jdbcResult.getString("end_date")
                if (jdbcResult.wasNull()) end_date = null

                var end_date_override: String? = jdbcResult.getString("end_date_override")
                if (jdbcResult.wasNull()) end_date_override = null

                var growth_plan: String? = jdbcResult.getString("growth_plan")
                if (jdbcResult.wasNull()) growth_plan = null

                var initial_end_date: String? = jdbcResult.getString("initial_end_date")
                if (jdbcResult.wasNull()) initial_end_date = null

                var intern: String? = jdbcResult.getString("intern")
                if (jdbcResult.wasNull()) intern = null

                var last_reactivated_at: String? = jdbcResult.getString("last_reactivated_at")
                if (jdbcResult.wasNull()) last_reactivated_at = null

                var mentor: String? = jdbcResult.getString("mentor")
                if (jdbcResult.wasNull()) mentor = null

                var methodologies: String? = jdbcResult.getString("methodologies")
                if (jdbcResult.wasNull()) methodologies = null

                var paratext_registry: String? = jdbcResult.getString("paratext_registry")
                if (jdbcResult.wasNull()) paratext_registry = null

                var periodic_reports_directory: String? = jdbcResult.getString("periodic_reports_directory")
                if (jdbcResult.wasNull()) periodic_reports_directory = null

                var position: String? = jdbcResult.getString("position")
                if (jdbcResult.wasNull()) position = null

                var sensitivity: String? = jdbcResult.getString("sensitivity")
                if (jdbcResult.wasNull()) sensitivity = null

                var start_date: String? = jdbcResult.getString("start_date")
                if (jdbcResult.wasNull()) start_date = null

                var start_date_override: String? = jdbcResult.getString("start_date_override")
                if (jdbcResult.wasNull()) start_date_override = null

                var status: String? = jdbcResult.getString("status")
                if (jdbcResult.wasNull()) status = null

                var status_modified_at: String? = jdbcResult.getString("status_modified_at")
                if (jdbcResult.wasNull()) status_modified_at = null

                var last_suspended_at: String? = jdbcResult.getString("last_suspended_at")
                if (jdbcResult.wasNull()) last_suspended_at = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    internshipEngagement(
                        id = id,

                        project = project,
                        change_to_plan = change_to_plan,
                        active = active,
                        ceremony = ceremony,
                        communications_complete_date = communications_complete_date,
                        complete_date = complete_date,
                        country_of_origin = country_of_origin,
                        disbursement_complete_date = disbursement_complete_date,
                        end_date = end_date,
                        end_date_override = end_date_override,
                        growth_plan = growth_plan,
                        initial_end_date = initial_end_date,
                        intern = intern,
                        last_reactivated_at = last_reactivated_at,
                        mentor = mentor,
                        methodologies = methodologies,
                        paratext_registry = paratext_registry,
                        periodic_reports_directory = periodic_reports_directory,
                        position = position,
                        sensitivity = sensitivity,
                        start_date = start_date,
                        start_date_override = start_date_override,
                        status = status,
                        status_modified_at = status_modified_at,
                        last_suspended_at = last_suspended_at,

                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScInternshipEngagementsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScInternshipEngagementsListResponse(ErrorType.NoError, data)
    }
}
