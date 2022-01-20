package com.seedcompany.cordtables.components.tables.sc.project_members

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_members.projectMemberInput
import com.seedcompany.cordtables.components.tables.sc.project_members.Read
import com.seedcompany.cordtables.components.tables.sc.project_members.Update
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectMembersCreateRequest(
    val token: String? = null,
    val projectMember: projectMemberInput,
)

data class ScProjectMembersCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectMembersCreate")
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

    @PostMapping("sc/project-members/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScProjectMembersCreateRequest): ScProjectMembersCreateResponse {

      if (req.token == null) return ScProjectMembersCreateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScProjectMembersCreateResponse(ErrorType.AdminOnly)
        // if (req.projectMember.name == null) return ScProjectMembersCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.project_members(project, person, group_id, role, sensitivity, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
                    ?::uuid,
                    ?::uuid,
                    ?::common.sensitivity,
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
            req.projectMember.project,
            req.projectMember.person,
            req.projectMember.group_id,
            req.projectMember.role,
            req.projectMember.sensitivity,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

//        req.language.id = id

        return ScProjectMembersCreateResponse(error = ErrorType.NoError, id = id)
    }

}
