package com.seedcompany.cordtables.components.tables.sc.partnerships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.partnerships.partnershipInput
import com.seedcompany.cordtables.components.tables.sc.partnerships.Read
import com.seedcompany.cordtables.components.tables.sc.partnerships.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPartnershipsCreateRequest(
    val token: String? = null,
    val partnership: partnershipInput,
)

data class ScPartnershipsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPartnershipsCreate")
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

    @PostMapping("sc/partnerships/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScPartnershipsCreateRequest): ScPartnershipsCreateResponse {

        // if (req.partnership.name == null) return ScPartnershipsCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.partnerships(project, partner, change_to_plan, active, agreement, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
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
            req.partnership.project,
            req.partnership.partner,
            req.partnership.change_to_plan,
            req.partnership.active,
            req.partnership.agreement,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return ScPartnershipsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
