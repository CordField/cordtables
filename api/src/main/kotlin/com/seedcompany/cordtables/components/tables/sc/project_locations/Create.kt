package com.seedcompany.cordtables.components.tables.sc.project_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_locations.projectLocationInput
import com.seedcompany.cordtables.components.tables.sc.project_locations.Read
import com.seedcompany.cordtables.components.tables.sc.project_locations.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectLocationsCreateRequest(
    val token: String? = null,
    val projectLocation: projectLocationInput,
)

data class ScProjectLocationsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectLocationsCreate")
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

    @PostMapping("sc/project-locations/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScProjectLocationsCreateRequest): ScProjectLocationsCreateResponse {

        // if (req.projectLocation.name == null) return ScProjectLocationsCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.project_locations(active, change_to_plan, location, project, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::boolean,
                    ?,
                    ?,
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
            req.projectLocation.active,
            req.projectLocation.change_to_plan,
            req.projectLocation.location,
            req.projectLocation.project,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

//        req.language.id = id

        return ScProjectLocationsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
