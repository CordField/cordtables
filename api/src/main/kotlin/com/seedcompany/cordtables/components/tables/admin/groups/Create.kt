package com.seedcompany.cordtables.components.tables.admin.groups

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.group_memberships.AdminGroupMembershipsUpdateResponse
import com.seedcompany.cordtables.components.tables.admin.groups.groupInput
import com.seedcompany.cordtables.components.tables.admin.groups.Read
import com.seedcompany.cordtables.components.tables.admin.groups.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupsCreateRequest(
    val token: String? = null,
    val group: groupInput,
)

data class AdminGroupsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupsCreate")
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

    @PostMapping("admin/groups/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminGroupsCreateRequest): AdminGroupsCreateResponse {

      if (req.token == null) return AdminGroupsCreateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminGroupsCreateResponse(ErrorType.AdminOnly)

        if (req.group.name == null) return AdminGroupsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into admin.groups(name, parent_group,  created_by, modified_by, owning_person, owning_group)
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
            req.group.name,
            req.group.parent_group,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

        return AdminGroupsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
