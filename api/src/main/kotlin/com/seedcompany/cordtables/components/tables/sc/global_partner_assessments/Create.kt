package com.seedcompany.cordtables.components.tables.sc.global_partner_assessments

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.globalPartnerAssessmentInput
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.Read
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource



data class ScGlobalPartnerAssessmentsCreateRequest(
    val token: String? = null,
    val globalPartnerAssessment: globalPartnerAssessmentInput,
)

data class ScGlobalPartnerAssessmentsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScGlobalPartnerAssessmentsCreate")
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

    @PostMapping("sc-global-partner-assessments/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScGlobalPartnerAssessmentsCreateRequest): ScGlobalPartnerAssessmentsCreateResponse {

        // if (req.globalPartnerAssessment.name == null) return ScGlobalPartnerAssessmentsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.global_partner_assessments(partner, governance_trans, director_trans, identity_trans, growth_trans, comm_support_trans, systems_trans, fin_management_trans, 
            hr_trans, it_trans, program_design_trans, tech_translation_trans, director_opp, financial_management_opp, program_design_opp,tech_translation_opp, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
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
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.globalPartnerAssessment.partner,
            req.globalPartnerAssessment.governance_trans,
            req.globalPartnerAssessment.director_trans,
            req.globalPartnerAssessment.identity_trans,
            req.globalPartnerAssessment.growth_trans,
            req.globalPartnerAssessment.comm_support_trans,
            req.globalPartnerAssessment.systems_trans,
            req.globalPartnerAssessment.fin_management_trans,
            req.globalPartnerAssessment.hr_trans,
            req.globalPartnerAssessment.it_trans,
            req.globalPartnerAssessment.program_design_trans,
            req.globalPartnerAssessment.tech_translation_trans,
            req.globalPartnerAssessment.director_opp,
            req.globalPartnerAssessment.financial_management_opp,
            req.globalPartnerAssessment.program_design_opp,
            req.globalPartnerAssessment.tech_translation_opp,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScGlobalPartnerAssessmentsCreateResponse(error = ErrorType.NoError, id = id)
    }

}