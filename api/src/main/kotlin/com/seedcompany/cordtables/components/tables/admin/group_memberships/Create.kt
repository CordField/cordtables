package com.seedcompany.cordtables.components.tables.admin.group_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.group_memberships.groupMembershipInput
import com.seedcompany.cordtables.components.tables.admin.group_memberships.Read
import com.seedcompany.cordtables.components.tables.admin.group_memberships.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupMembershipsCreateRequest(
    val token: String? = null,
    val groupMembership: groupMembershipInput,
)

data class AdminGroupMembershipsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminGroupMembershipsCreate")
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

    @PostMapping("admin-group-memberships/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminGroupMembershipsCreateRequest): AdminGroupMembershipsCreateResponse {

        // if (req.groupMembership.name == null) return AdminGroupMembershipsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into admin.group_memberships(group_id, person,  created_by, modified_by, owning_person, owning_group)
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
            req.groupMembership.group_id,
            req.groupMembership.person,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return AdminGroupMembershipsCreateResponse(error = ErrorType.NoError, id = id)
    }

}