package com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.globalPartnerEngagementPeopleInput
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.Read
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerEngagementPeopleCreateRequest(
    val token: String? = null,
    val globalPartnerEngagementPeople: globalPartnerEngagementPeopleInput,
)

data class ScGlobalPartnerEngagementPeopleCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerEngagementPeopleCreate")
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

    @PostMapping("sc/global-partner-engagement-people/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScGlobalPartnerEngagementPeopleCreateRequest): ScGlobalPartnerEngagementPeopleCreateResponse {

       //  if (req.globalPartnerEngagementPeople.name == null) return ScGlobalPartnerEngagementPeopleCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.global_partner_engagement_people(engagement, person, role, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?::common.people_to_org_relationship_type,
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
            req.globalPartnerEngagementPeople.engagement,
            req.globalPartnerEngagementPeople.person,
            req.globalPartnerEngagementPeople.role,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return ScGlobalPartnerEngagementPeopleCreateResponse(error = ErrorType.NoError, id = id)
    }

}
