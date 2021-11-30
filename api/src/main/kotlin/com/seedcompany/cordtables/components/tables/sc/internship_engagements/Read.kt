package com.seedcompany.cordtables.components.tables.sc.internship_engagements

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.internship_engagements.internshipEngagement
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocation
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

data class ScInternshipEngagementsReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class ScInternshipEngagementsReadResponse(
    val error: ErrorType,
    val internshipEngagement: internshipEngagement? = null,
)


@Controller("ScInternshipEngagementsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-internship-engagements/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScInternshipEngagementsReadRequest): ScInternshipEngagementsReadResponse {

        if (req.token == null) return ScInternshipEngagementsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScInternshipEngagementsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.internship_engagements",
                getList = false,
                columns = arrayOf(
                    "id",
                    "project",
                    "ethnologue",
                    "change_to_plan",
                    "active",
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
                    "methodology",
                    "paratext_registry",
                    "periodic_reports_directory",
                    "position",
                    "start_date",
                    "start_date_override",
                    "status",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var project: Int? = jdbcResult.getInt("project")
                if (jdbcResult.wasNull()) project = null

                var ethnologue: Int? = jdbcResult.getInt("ethnologue")
                if (jdbcResult.wasNull()) ethnologue = null

                var change_to_plan: Int? = jdbcResult.getInt("change_to_plan")
                if (jdbcResult.wasNull()) change_to_plan = null

                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null

                var communications_complete_date: String? = jdbcResult.getString("communications_complete_date")
                if (jdbcResult.wasNull()) communications_complete_date = null

                var complete_date: String? = jdbcResult.getString("complete_date")
                if (jdbcResult.wasNull()) complete_date = null

                var country_of_origin: Int? = jdbcResult.getInt("country_of_origin")
                if (jdbcResult.wasNull()) country_of_origin = null

                var disbursement_complete_date: String? = jdbcResult.getString("disbursement_complete_date")
                if (jdbcResult.wasNull()) disbursement_complete_date = null

                var end_date: String? = jdbcResult.getString("end_date")
                if (jdbcResult.wasNull()) end_date = null

                var end_date_override: String? = jdbcResult.getString("end_date_override")
                if (jdbcResult.wasNull()) end_date_override = null

                var growth_plan: Int? = jdbcResult.getInt("growth_plan")
                if (jdbcResult.wasNull()) growth_plan = null

                var initial_end_date: String? = jdbcResult.getString("initial_end_date")
                if (jdbcResult.wasNull()) initial_end_date = null

                var intern: Int? = jdbcResult.getInt("intern")
                if (jdbcResult.wasNull()) intern = null

                var last_reactivated_at: String? = jdbcResult.getString("last_reactivated_at")
                if (jdbcResult.wasNull()) last_reactivated_at = null

                var mentor: Int? = jdbcResult.getInt("mentor")
                if (jdbcResult.wasNull()) mentor = null

                var methodology: String? = jdbcResult.getString("methodology")
                if (jdbcResult.wasNull()) methodology = null

                var paratext_registry: String? = jdbcResult.getString("paratext_registry")
                if (jdbcResult.wasNull()) paratext_registry = null

                var periodic_reports_directory: Int? = jdbcResult.getInt("periodic_reports_directory")
                if (jdbcResult.wasNull()) periodic_reports_directory = null

                var position: String? = jdbcResult.getString("position")
                if (jdbcResult.wasNull()) position = null

                var start_date: String? = jdbcResult.getString("start_date")
                if (jdbcResult.wasNull()) start_date = null

                var start_date_override: String? = jdbcResult.getString("start_date_override")
                if (jdbcResult.wasNull()) start_date_override = null

                var status: String? = jdbcResult.getString("status")
                if (jdbcResult.wasNull()) status = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val internshipEngagement =
                    internshipEngagement(
                        id = id,

                        project = project,
                        ethnologue = ethnologue,
                        change_to_plan = change_to_plan,
                        active = active,
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
                        methodology = methodology,
                        paratext_registry = paratext_registry,
                        periodic_reports_directory = periodic_reports_directory,
                        position = position,
                        start_date = start_date,
                        start_date_override = start_date_override,
                        status = status,

                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScInternshipEngagementsReadResponse(ErrorType.NoError, internshipEngagement = internshipEngagement)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScInternshipEngagementsReadResponse(ErrorType.SQLReadError)
        }

        return ScInternshipEngagementsReadResponse(error = ErrorType.UnknownError)
    }
}