package com.seedcompany.cordtables.components.tables.sc.projects

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.projects.project
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


data class ScProjectsListRequest(
    val token: String?
)

data class ScProjectsListResponse(
    val error: ErrorType,
    val projects: MutableList<project>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/projects/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScProjectsListRequest): ScProjectsListResponse {
        var data: MutableList<project> = mutableListOf()
        if (req.token == null) return ScProjectsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.projects",
                filter = "order by id",
                columns = arrayOf(
                    "id",

                    "neo4j_id",
                    "name",
                    "change_to_plan",
                    "active",
                    "department",
                    "estimated_submission",
                    "field_region",
                    "initial_mou_end",
                    "marketing_location",
                    "mou_start",
                    "mou_end",
                    "owning_organization",
                    "periodic_reports_directory",
                    "posts_directory",
                    "primary_location",
                    "root_directory",
                    "status",
                    "status_changed_at",
                    "step",


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

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null


                var neo4j_id: String? = jdbcResult.getString("neo4j_id")
                if (jdbcResult.wasNull()) neo4j_id = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

                var change_to_plan: Int? = jdbcResult.getInt("change_to_plan")
                if (jdbcResult.wasNull()) change_to_plan = null

                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null

                var department: String? = jdbcResult.getString("department")
                if (jdbcResult.wasNull()) department = null

                var estimated_submission: String? = jdbcResult.getString("estimated_submission")
                if (jdbcResult.wasNull()) estimated_submission = null

                var field_region: Int? = jdbcResult.getInt("field_region")
                if (jdbcResult.wasNull()) field_region = null

                var initial_mou_end: String? = jdbcResult.getString("initial_mou_end")
                if (jdbcResult.wasNull()) initial_mou_end = null

                var marketing_location: Int? = jdbcResult.getInt("marketing_location")
                if (jdbcResult.wasNull()) marketing_location = null

                var mou_start: String? = jdbcResult.getString("mou_start")
                if (jdbcResult.wasNull()) mou_start = null

                var mou_end: String? = jdbcResult.getString("mou_end")
                if (jdbcResult.wasNull()) mou_end = null

                var owning_organization: Int? = jdbcResult.getInt("owning_organization")
                if (jdbcResult.wasNull()) owning_organization = null

                var periodic_reports_directory: Int? = jdbcResult.getInt("periodic_reports_directory")
                if (jdbcResult.wasNull()) periodic_reports_directory = null

                var posts_directory: Int? = jdbcResult.getInt("posts_directory")
                if (jdbcResult.wasNull()) posts_directory = null

                var primary_location: Int? = jdbcResult.getInt("primary_location")
                if (jdbcResult.wasNull()) primary_location = null

                var root_directory: Int? = jdbcResult.getInt("root_directory")
                if (jdbcResult.wasNull()) root_directory = null

                var status: String? = jdbcResult.getString("status")
                if (jdbcResult.wasNull()) status = null

                var status_changed_at: String? = jdbcResult.getString("status_changed_at")
                if (jdbcResult.wasNull()) status_changed_at = null

                var step: String? = jdbcResult.getString("step")
                if (jdbcResult.wasNull()) step = null




                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    project(
                        id = id,

                        neo4j_id = neo4j_id,
                        name = name,
                        change_to_plan = change_to_plan,
                        active = active,
                        department = department,
                        estimated_submission = estimated_submission,
                        field_region = field_region,
                        initial_mou_end = initial_mou_end,
                        marketing_location = marketing_location,
                        mou_start = mou_start,
                        mou_end = mou_end,
                        owning_organization = owning_organization,
                        periodic_reports_directory = periodic_reports_directory,
                        posts_directory = posts_directory,
                        primary_location = primary_location,
                        root_directory = root_directory,
                        status = status,
                        status_changed_at = status_changed_at,
                        step = step,


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
            return ScProjectsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScProjectsListResponse(ErrorType.NoError, data)
    }
}

