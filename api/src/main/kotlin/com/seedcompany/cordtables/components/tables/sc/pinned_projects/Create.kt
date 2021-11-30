package com.seedcompany.cordtables.components.tables.sc.pinned_projects

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.pinnedProjectInput
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.Read
import com.seedcompany.cordtables.components.tables.sc.pinned_projects.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPinnedProjectsCreateRequest(
    val token: String? = null,
    val pinnedProject: pinnedProjectInput,
)

data class ScPinnedProjectsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)


@Controller("ScPinnedProjectsCreate")
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

    @PostMapping("sc-pinned-projects/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScPinnedProjectsCreateRequest): ScPinnedProjectsCreateResponse {

        // if (req.pinnedProject.name == null) return ScPinnedProjectsCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.pinned_projects(person, project,  created_by, modified_by, owning_person, owning_group)
                values(
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
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.pinnedProject.person,
            req.pinnedProject.project,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScPinnedProjectsCreateResponse(error = ErrorType.NoError, id = id)
    }

}