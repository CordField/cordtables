package com.seedcompany.cordtables.components.tables.sc.partners

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.budget_records.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPartnersCreateRequest(
    val token: String? = null,
    val partner: partnerInput,
)

data class ScPartnersCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPartnersCreate")
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

    @PostMapping("sc/partners/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScPartnersCreateRequest): ScPartnersCreateResponse {

        // if (req.partner.name == null) return ScPartnersCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.partners(id, active, financial_reporting_types,  is_innovations_client, pmc_entity_code, point_of_contact,
             types, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::boolean,
                    ARRAY[?]::sc.financial_reporting_types[],
                    ?,
                    ?,
                    ?::uuid,
                    ARRAY[?]::sc.partner_types[],
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
                    (33322
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.partner.id,
            req.partner.active,
            req.partner.financial_reporting_types,
            req.partner.is_innovations_client,
            req.partner.pmc_entity_code,
            req.partner.point_of_contact,
            req.partner.types,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return ScPartnersCreateResponse(error = ErrorType.NoError, id = id)
    }

}
