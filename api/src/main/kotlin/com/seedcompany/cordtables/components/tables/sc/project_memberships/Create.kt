package com.seedcompany.cordtables.components.tables.sc.project_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_memberships.projectMembershipInput
import com.seedcompany.cordtables.components.tables.sc.project_memberships.Read
import com.seedcompany.cordtables.components.tables.sc.project_memberships.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectMembershipsCreateRequest(
    val token: String? = null,
    val projectMembership: projectMembershipInput,
)

data class ScProjectMembershipsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectMembershipsCreate")
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

    @PostMapping("sc/project-memberships/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScProjectMembershipsCreateRequest): ScProjectMembershipsCreateResponse {

        // if (req.projectMembership.name == null) return ScProjectMembershipsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.project_memberships(group_id, person,  created_by, modified_by, owning_person, owning_group)
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
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.projectMembership.group_id,
            req.projectMembership.person,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

        return ScProjectMembershipsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
