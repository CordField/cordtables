package com.seedcompany.cordtables.components.pages.partners

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.GetPaginatedResultSet
import com.seedcompany.cordtables.common.GetPaginatedResultSetRequest
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.common.discussion_channels.CommonDiscussionChannelsListResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource
import javax.sql.rowset.serial.SerialArray

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("PartnersController")
class Controller (
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureResultSet: GetPaginatedResultSet,

  @Autowired
  val secureList: GetSecureListQuery,
) {
    // val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @CrossOrigin(origins =  ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
    @PostMapping("sc/partners-crm/list")
    @ResponseBody
    fun list(@RequestBody req: PartnersRequest): PartnersResponse{
        var data: MutableList<Partner> = mutableListOf()
        var error: ErrorType = ErrorType.NoError
        var size:Int = 0
        if (req.token == null) return PartnersResponse(ErrorType.TokenNotFound, size = size, partners = mutableListOf())

        var columnsNew = mutableMapOf(
            "sc.partners" to mutableMapOf(
                "id" to "id",
                "active" to "active",
                "financial_reporting_types" to "financial_reporting_types",
                "is_innovations_client" to "is_innovations_client",
                "pmc_entity_code" to "pmc_entity_code",
                "point_of_contact" to "point_of_contact",
                "types" to "types",
                "address" to "address",
                "sensitivity" to "partner_sensitivity"
            ),
            "common.organizations" to mutableMapOf(
                "id" to "org_id",
                "name" to "name",
                "sensitivity" to "sensitivity",
                "primary_location" to "primary_location",
                "created_at" to "created_at",
                "modified_at" to "modified_at"
            )
        )

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        try {
            val resultSet = secureResultSet.getPaginatedResultSetHandler(
                GetPaginatedResultSetRequest(
                    tableName = "sc.partners",
                    filter = "order by id",
                    token = req.token,
                    page = req.page!!,
                    resultsPerPage = req.resultsPerPage!!,
                    joinColumns = columnsNew,
                    joinTables = "sc.partners LEFT JOIN common.organizations ON sc.partners.id=common.organizations.id",
                    searchColumns = arrayOf(
                        "common.organizations.name",
                        "sc.partners.address"
                    ),
                    searchKeyword = req.search!!,
                    columns = arrayOf()
                )
            )
            val jdbcResult = resultSet.result
            size = resultSet.size

            // val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult!!.next()){
                data.add(
                    Partner(
                        id = jdbcResult.getString("id"),
                        active = jdbcResult.getBoolean("active"),
                        financial_reporting_types = jdbcResult.getString("financial_reporting_types"),
                        is_innovations_client = jdbcResult.getString("is_innovations_client"),
                        pmc_entity_code = jdbcResult.getString("pmc_entity_code"),
                        point_of_contact = jdbcResult.getString("point_of_contact"),
                        types = jdbcResult.getString("types"),
                        address = jdbcResult.getString("address"),
                        partner_sensitivity = jdbcResult.getString("partner_sensitivity"),
                        name = jdbcResult.getString("name"),
                        sensitivity = jdbcResult.getString("sensitivity"),
                        primary_location = jdbcResult.getString("primary_location"),
                        created_at = jdbcResult.getString("created_at"),
                        modified_at = jdbcResult.getString("modified_at"),
                   )
                )
            }
        }
        catch (e: SQLException){
            println(e.message)
            error = ErrorType.SQLReadError
        }
        return PartnersResponse(error = error, size = size, partners = data)
    }

    @CrossOrigin(origins =  ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
    @PostMapping("sc/partners-crm/read")
    @ResponseBody
    fun read(@RequestBody req: PartnersRequest): PartnerReadResponse{
        val error = ErrorType.NoError
        var partner: Partner? = null

        var columnsNew = mutableMapOf(
            "sc.partners" to mutableMapOf(
                "id" to "id",
                "active" to "active",
                "financial_reporting_types" to "financial_reporting_types",
                "is_innovations_client" to "is_innovations_client",
                "pmc_entity_code" to "pmc_entity_code",
                "point_of_contact" to "point_of_contact",
                "types" to "types",
                "address" to "address",
                "sensitivity" to "partner_sensitivity"
            ),
            "common.organizations" to mutableMapOf(
                "name" to "name",
                "sensitivity" to "sensitivity",
                "primary_location" to "primary_location",
                "created_at" to "created_at",
                "modified_at" to "modified_at"
            ),
            "sc.global_partner_assessments" to mutableMapOf(
                "id" to "gpaid",
                "partner" to "partner",
                "governance_trans" to "governance_trans",
                "director_trans" to "director_trans",
                "identity_trans" to "identity_trans",
                "growth_trans" to "growth_trans",
                "comm_support_trans" to "comm_support_trans",
                "systems_trans" to "systems_trans",
                "fin_management_trans" to "fin_management_trans",
                "hr_trans" to "hr_trans",
                "it_trans" to "it_trans",
                "program_design_trans" to "program_design_trans",
                "tech_translation_trans" to "tech_translation_trans",
                "director_opp" to "director_opp",
                "financial_management_opp" to "financial_management_opp",
                "program_design_opp" to "program_design_opp",
                "tech_translation_opp" to "tech_translation_opp"
            ),
            "sc.global_partner_performance" to mutableMapOf(
                "reporting_performance" to "reporting_performance",
                "financial_performance" to "financial_performance",
                "translation_performance" to "translation_performance"
            )
        )

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)
        val joinString = """
             sc.partners
                  LEFT JOIN common.organizations ON sc.partners.id=common.organizations.id
                  LEFT JOIN sc.global_partner_assessments ON sc.partners.id=sc.global_partner_assessments.partner
                  LEFT JOIN sc.global_partner_performance ON sc.partners.id=sc.global_partner_performance.organization
        """.trimIndent()
        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.partners",
                filter = "order by id",
                getList = false,
                joinColumns = columnsNew,
                joinTables = joinString,
                columns = arrayOf()
            )
        ).query

        try {
            var jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()){
                partner = Partner(
                    id = jdbcResult.getString("id"),
                    active = jdbcResult.getBoolean("active"),
                    financial_reporting_types = jdbcResult.getString("financial_reporting_types"),
                    is_innovations_client = jdbcResult.getString("is_innovations_client"),
                    pmc_entity_code = jdbcResult.getString("pmc_entity_code"),
                    point_of_contact = jdbcResult.getString("point_of_contact"),
                    types = jdbcResult.getString("types"),
                    address = jdbcResult.getString("address"),
                    partner_sensitivity = jdbcResult.getString("partner_sensitivity"),
                    name = jdbcResult.getString("name"),
                    sensitivity = jdbcResult.getString("sensitivity"),
                    primary_location = jdbcResult.getString("primary_location"),
                    gpaid = jdbcResult.getString("gpaid"),
                    partner = jdbcResult.getString("partner"),
                    governance_trans = jdbcResult.getString("governance_trans"),
                    director_trans = jdbcResult.getString("director_trans"),
                    identity_trans = jdbcResult.getString("identity_trans"),
                    growth_trans = jdbcResult.getString("growth_trans"),
                    comm_support_trans = jdbcResult.getString("comm_support_trans"),
                    systems_trans = jdbcResult.getString("systems_trans"),
                    fin_management_trans = jdbcResult.getString("fin_management_trans"),
                    hr_trans = jdbcResult.getString("hr_trans"),
                    it_trans = jdbcResult.getString("it_trans"),
                    program_design_trans = jdbcResult.getString("program_design_trans"),
                    tech_translation_trans = jdbcResult.getString("tech_translation_trans"),
                    director_opp = jdbcResult.getString("director_opp"),
                    financial_management_opp = jdbcResult.getString("financial_management_opp"),
                    program_design_opp = jdbcResult.getString("program_design_opp"),
                    tech_translation_opp = jdbcResult.getString("tech_translation_opp"),
                    reporting_performance = jdbcResult.getString("reporting_performance"),
                    financial_performance = jdbcResult.getString("financial_performance"),
                    translation_performance = jdbcResult.getString("translation_performance"),
                    created_at = jdbcResult.getString("created_at"),
                    modified_at = jdbcResult.getString("modified_at"),
                )
            }
        }
        catch (e: SQLException){
            println("SQL ERROR: ${e.message}")
        }
        return PartnerReadResponse(error = error, partner = partner)
    }

    fun getEngagements(partner: String, token: String){
        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", token)
        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.global_partner_engagements",
                filter = "order by id",
                whereClause = "organization='$partner'",
                columns = arrayOf(
                    "id",
                    "organization",
                    "type",
                    "mou_start",
                    "mou_end",
                    "sc_roles",
                    "partner_roles",
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
        }
        catch (e: SQLException){

        }
    }

}
