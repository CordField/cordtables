package com.seedcompany.cordtables.components.tables.sc.global_partner_engagements

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.globalPartnerEngagementInput
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.Read
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerEngagementsCreateRequest(
    val token: String? = null,
    val globalPartnerEngagement: globalPartnerEngagementInput,
)

data class ScGlobalPartnerEngagementsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScGlobalPartnerEngagementsCreate")
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

    @PostMapping("sc-global-partner-engagements/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScGlobalPartnerEngagementsCreateRequest): ScGlobalPartnerEngagementsCreateResponse {

        // if (req.globalPartnerEngagement.name == null) return ScGlobalPartnerEngagementsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.global_partner_engagements(organization, type, mou_start, mou_end, sc_roles, partner_roles, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::common.involvement_options,
                    ?::timestamp,
                    ?::timestamp,
                    ARRAY[?]::sc.global_partner_roles[],
                    ARRAY[?]::sc.global_partner_roles[],
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
            req.globalPartnerEngagement.organization,
            req.globalPartnerEngagement.type,
            req.globalPartnerEngagement.mou_start,
            req.globalPartnerEngagement.mou_end,
            req.globalPartnerEngagement.sc_roles,
            req.globalPartnerEngagement.partner_roles,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScGlobalPartnerEngagementsCreateResponse(error = ErrorType.NoError, id = id)
    }

}