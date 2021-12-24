package com.seedcompany.cordtables.components.tables.sc.global_partner_assessments

import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.globalPartnerAssessment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class ScGlobalPartnerAssessmentsListRequest(
    val token: String?
)

data class ScGlobalPartnerAssessmentsListResponse(
    val error: ErrorType,
    val globalPartnerAssessments: MutableList<globalPartnerAssessment>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerAssessmentsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-global-partner-assessments/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScGlobalPartnerAssessmentsListRequest): ScGlobalPartnerAssessmentsListResponse {
        var data: MutableList<globalPartnerAssessment> = mutableListOf()
        if (req.token == null) return ScGlobalPartnerAssessmentsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.global_partner_assessments",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "partner",
                    "governance_trans",
                    "director_trans",
                    "identity_trans",
                    "growth_trans",
                    "comm_support_trans",
                    "systems_trans",
                    "fin_management_trans",
                    "hr_trans",
                    "it_trans",
                    "program_design_trans",
                    "tech_translation_trans",
                    "director_opp",
                    "financial_management_opp",
                    "program_design_opp",
                    "tech_translation_opp",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var partner: String? = jdbcResult.getString("partner")
                if (jdbcResult.wasNull()) partner = null

                var governance_trans: String? = jdbcResult.getString("governance_trans")
                if (jdbcResult.wasNull()) governance_trans = null

                var director_trans: String? = jdbcResult.getString("director_trans")
                if (jdbcResult.wasNull()) director_trans = null

                var identity_trans: String? = jdbcResult.getString("identity_trans")
                if (jdbcResult.wasNull()) identity_trans = null

                var growth_trans: String? = jdbcResult.getString("growth_trans")
                if (jdbcResult.wasNull()) growth_trans = null

                var comm_support_trans: String? = jdbcResult.getString("comm_support_trans")
                if (jdbcResult.wasNull()) comm_support_trans = null

                var systems_trans: String? = jdbcResult.getString("systems_trans")
                if (jdbcResult.wasNull()) systems_trans = null

                var fin_management_trans: String? = jdbcResult.getString("fin_management_trans")
                if (jdbcResult.wasNull()) fin_management_trans = null

                var hr_trans: String? = jdbcResult.getString("hr_trans")
                if (jdbcResult.wasNull()) hr_trans = null

                var it_trans: String? = jdbcResult.getString("it_trans")
                if (jdbcResult.wasNull()) it_trans = null

                var program_design_trans: String? = jdbcResult.getString("program_design_trans")
                if (jdbcResult.wasNull()) program_design_trans = null

                var tech_translation_trans: String? = jdbcResult.getString("tech_translation_trans")
                if (jdbcResult.wasNull()) tech_translation_trans = null

                var director_opp: String? = jdbcResult.getString("director_opp")
                if (jdbcResult.wasNull()) director_opp = null

                var financial_management_opp: String? = jdbcResult.getString("financial_management_opp")
                if (jdbcResult.wasNull()) financial_management_opp = null

                var program_design_opp: String? = jdbcResult.getString("program_design_opp")
                if (jdbcResult.wasNull()) program_design_opp = null

                var tech_translation_opp: String? = jdbcResult.getString("tech_translation_opp")
                if (jdbcResult.wasNull()) tech_translation_opp = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    globalPartnerAssessment(
                        id = id,
                        partner = partner,
                        governance_trans = governance_trans, //if (governance_trans == null) null else PartnerMaturityScale.valueOf(governance_trans),
                        director_trans = director_trans, // if (director_trans == null) null else PartnerMaturityScale.valueOf(director_trans),
                        identity_trans = identity_trans, // if (identity_trans == null) null else PartnerMaturityScale.valueOf(identity_trans),
                        growth_trans = growth_trans, // if (growth_trans == null) null else PartnerMaturityScale.valueOf(growth_trans),
                        comm_support_trans = comm_support_trans, // if (comm_support_trans == null) null else PartnerMaturityScale.valueOf(comm_support_trans),
                        systems_trans = systems_trans, // if (systems_trans == null) null else PartnerMaturityScale.valueOf(systems_trans),
                        fin_management_trans =  fin_management_trans, //if (fin_management_trans == null) null else PartnerMaturityScale.valueOf(fin_management_trans),
                        hr_trans = hr_trans, // if (hr_trans == null) null else PartnerMaturityScale.valueOf(hr_trans),
                        it_trans = it_trans, // if (it_trans == null) null else PartnerMaturityScale.valueOf(it_trans),
                        program_design_trans = program_design_trans, //if (program_design_trans == null) null else PartnerMaturityScale.valueOf(program_design_trans),
                        tech_translation_trans = tech_translation_trans, // if (tech_translation_trans == null) null else PartnerMaturityScale.valueOf(tech_translation_trans),
                        director_opp = director_opp, // if (director_opp == null) null else PartnerMaturityScale.valueOf(director_opp),
                        financial_management_opp = financial_management_opp, //if (financial_management_opp == null) null else PartnerMaturityScale.valueOf(financial_management_opp),
                        program_design_opp = program_design_opp, // if (program_design_opp == null) null else PartnerMaturityScale.valueOf(program_design_opp),
                        tech_translation_opp = tech_translation_opp, //if (tech_translation_opp == null) null else PartnerMaturityScale.valueOf(tech_translation_opp),
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScGlobalPartnerAssessmentsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScGlobalPartnerAssessmentsListResponse(ErrorType.NoError, data)
    }
}
