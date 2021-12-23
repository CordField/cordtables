package com.seedcompany.cordtables.components.tables.sc.language_engagements

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.language_engagements.languageEngagement
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

data class ScLanguageEngagementsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScLanguageEngagementsReadResponse(
    val error: ErrorType,
    val languageEngagement: languageEngagement? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageEngagementsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-language-engagements/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScLanguageEngagementsReadRequest): ScLanguageEngagementsReadResponse {

        if (req.token == null) return ScLanguageEngagementsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScLanguageEngagementsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.language_engagements",
                getList = false,
                columns = arrayOf(
                    "id",
                    "neo4j_id",
                    "project",
                    "ethnologue",
                    "change_to_plan",
                    "active",
                    "communications_complete_date",
                    "complete_date",
                    "disbursement_complete_date",
                    "end_date",
                    "end_date_override",
                    "initial_end_date",
                    "is_first_scripture",
                    "is_luke_partnership",
                    "is_sent_printing",
                    "last_reactivated_at",
                    "paratext_registry",
                    "periodic_reports_directory",
                    "pnp",
                    "pnp_file",
                    "product_engagement_tag",
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

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var neo4j_id: String? = jdbcResult.getString("neo4j_id")
                if (jdbcResult.wasNull()) neo4j_id = null

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

                var disbursement_complete_date: String? = jdbcResult.getString("disbursement_complete_date")
                if (jdbcResult.wasNull()) disbursement_complete_date = null

                var end_date: String? = jdbcResult.getString("end_date")
                if (jdbcResult.wasNull()) end_date = null

                var end_date_override: String? = jdbcResult.getString("end_date_override")
                if (jdbcResult.wasNull()) end_date_override = null

                var initial_end_date: String? = jdbcResult.getString("initial_end_date")
                if (jdbcResult.wasNull()) initial_end_date = null

                var is_first_scripture: Boolean? = jdbcResult.getBoolean("is_first_scripture")
                if (jdbcResult.wasNull()) is_first_scripture = null

                var is_luke_partnership: Boolean? = jdbcResult.getBoolean("is_luke_partnership")
                if (jdbcResult.wasNull()) is_luke_partnership = null

                var is_sent_printing: Boolean? = jdbcResult.getBoolean("is_sent_printing")
                if (jdbcResult.wasNull()) is_sent_printing = null

                var last_reactivated_at: String? = jdbcResult.getString("last_reactivated_at")
                if (jdbcResult.wasNull()) last_reactivated_at = null

                var paratext_registry: String? = jdbcResult.getString("paratext_registry")
                if (jdbcResult.wasNull()) paratext_registry = null

                var periodic_reports_directory: Int? = jdbcResult.getInt("periodic_reports_directory")
                if (jdbcResult.wasNull()) periodic_reports_directory = null

                var pnp: String? = jdbcResult.getString("pnp")
                if (jdbcResult.wasNull()) pnp = null

                var pnp_file: Int? = jdbcResult.getInt("pnp_file")
                if (jdbcResult.wasNull()) pnp_file = null

                var product_engagement_tag: String? = jdbcResult.getString("product_engagement_tag")
                if (jdbcResult.wasNull()) product_engagement_tag = null

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

                val languageEngagement =
                    languageEngagement(
                        id = id,
                        neo4j_id = neo4j_id,
                        project = project,
                        ethnologue = ethnologue,
                        change_to_plan = change_to_plan,
                        active = active,
                        communications_complete_date = communications_complete_date,
                        complete_date = complete_date,
                        disbursement_complete_date = disbursement_complete_date,
                        end_date = end_date,
                        end_date_override = end_date_override,
                        initial_end_date = initial_end_date,
                        is_first_scripture = is_first_scripture,
                        is_luke_partnership = is_luke_partnership,
                        is_sent_printing = is_sent_printing,
                        last_reactivated_at = last_reactivated_at,
                        paratext_registry = paratext_registry,
                        periodic_reports_directory = periodic_reports_directory,
                        pnp = pnp,
                        pnp_file = pnp_file,
                        product_engagement_tag = product_engagement_tag,
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

                return ScLanguageEngagementsReadResponse(ErrorType.NoError, languageEngagement = languageEngagement)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScLanguageEngagementsReadResponse(ErrorType.SQLReadError)
        }

        return ScLanguageEngagementsReadResponse(error = ErrorType.UnknownError)
    }
}
