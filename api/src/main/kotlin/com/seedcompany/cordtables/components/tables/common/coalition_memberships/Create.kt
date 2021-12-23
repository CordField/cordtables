package com.seedcompany.cordtables.components.tables.common.coalition_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.coalitionMembershipInput
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.Read
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCoalitionMembershipsCreateRequest(
    val token: String? = null,
    val coalitionMembership: coalitionMembershipInput,
)

data class CommonCoalitionMembershipsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCoalitionMembershipsCreate")
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

    @PostMapping("common/coalition-memberships/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonCoalitionMembershipsCreateRequest): CommonCoalitionMembershipsCreateResponse {

        // if (req.coalitionMembership.name == null) return CommonCoalitionMembershipsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.coalition_memberships(coalition, organization, created_by, modified_by, owning_person, owning_group)
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
            String::class.java,
            req.coalitionMembership.coalition,
            req.coalitionMembership.organization,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return CommonCoalitionMembershipsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
