package com.seedcompany.cordtables.components.tables.sc.projects

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.projects.projectInput
import com.seedcompany.cordtables.components.tables.sc.projects.Read
import com.seedcompany.cordtables.components.tables.sc.projects.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectsCreateRequest(
    val token: String? = null,
    val project: projectInput,
)

data class ScProjectsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScProjectsCreate")
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

    @PostMapping("sc-projects/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScProjectsCreateRequest): ScProjectsCreateResponse {

        // if (req.project.name == null) return ScProjectsCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.projects(neo4j_id, name, change_to_plan, active, department, estimated_submission, field_region, initial_mou_end, marketing_location,
             mou_start, mou_end, owning_organization, periodic_reports_directory, posts_directory, primary_location, root_directory, status, status_changed_at, step, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?::timestamp,
                    ?,
                    ?::timestamp,
                    ?,
                    ?::timestamp,
                    ?::timestamp,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?::sc.project_status,
                    ?::timestamp,
                    ?::sc.project_step,
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
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.project.neo4j_id,
            req.project.name,
            req.project.change_to_plan,
            req.project.active,
            req.project.department,
            req.project.estimated_submission,
            req.project.field_region,
            req.project.initial_mou_end,
            req.project.marketing_location,
            req.project.mou_start,
            req.project.mou_end,
            req.project.owning_organization,
            req.project.periodic_reports_directory,
            req.project.posts_directory,
            req.project.primary_location,
            req.project.root_directory,
            req.project.status,
            req.project.status_changed_at,
            req.project.step,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScProjectsCreateResponse(error = ErrorType.NoError, id = id)
    }

}