package com.seedcompany.cordtables.components.tables.sc.global_partner_transitions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.globalPartnerTransitionInput
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.Read
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerTransitionsCreateRequest(
    val token: String? = null,
    val globalPartnerTransition: globalPartnerTransitionInput,
)

data class ScGlobalPartnerTransitionsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerTransitionsCreate")
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

    @PostMapping("sc/global-partner-transitions/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScGlobalPartnerTransitionsCreateRequest): ScGlobalPartnerTransitionsCreateResponse {

        // if (req.globalPartnerTransition.name == null) return ScGlobalPartnerTransitionsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.global_partner_transitions(organization, transition_type, effective_date, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::sc.global_partner_transition_options,
                    ?::timestamp,
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
            req.globalPartnerTransition.organization,
            req.globalPartnerTransition.transition_type,
            req.globalPartnerTransition.effective_date,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return ScGlobalPartnerTransitionsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
