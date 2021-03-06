package com.seedcompany.cordtables.components.pages.partners

import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
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
  val secureResultSetV2: GetPaginatedResultSetV2,

  @Autowired
  val secureList: GetSecureListQuery,
) {
    // val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)
    val jdbcTemplate2: JdbcTemplate = JdbcTemplate(ds)

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

        try {
            val queryObj = secureResultSet.getPaginatedResultSetHandler(
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
            var dbResultSet = queryObj.result
            size = queryObj.size
            while (dbResultSet!!.next()){
                var financial_reporting_types:Array<out Any>? =  (dbResultSet.getObject("financial_reporting_types") as? SerialArray)?.array as Array<out Any>?

                data.add(
                    Partner(
                        id = dbResultSet.getString("id"),
                        active = dbResultSet.getBoolean("active"),
                        financial_reporting_types = financial_reporting_types,
                        is_innovations_client = dbResultSet.getBoolean("is_innovations_client"),
                        pmc_entity_code = dbResultSet.getString("pmc_entity_code"),
                        point_of_contact = dbResultSet.getString("point_of_contact"),
                        types = dbResultSet.getString("types"),
                        address = dbResultSet.getString("address"),
                        partner_sensitivity = dbResultSet.getString("partner_sensitivity"),
                        name = dbResultSet.getString("name"),
                        sensitivity = dbResultSet.getString("sensitivity"),
                        primary_location = dbResultSet.getString("primary_location"),
                        created_at = dbResultSet.getString("created_at"),
                        modified_at = dbResultSet.getString("modified_at")
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
                "id" to "gppid",
                "reporting_performance" to "reporting_performance",
                "financial_performance" to "financial_performance",
                "translation_performance" to "translation_performance"
            )
        )



        val joinString = """
             sc.partners
                  LEFT JOIN common.organizations ON sc.partners.id=common.organizations.id
                  LEFT JOIN sc.global_partner_assessments ON sc.partners.id=sc.global_partner_assessments.partner
                  LEFT JOIN sc.global_partner_performance ON sc.partners.id=sc.global_partner_performance.organization
        """.trimIndent()

        val jdbcResult = secureResultSet.getPaginatedResultSetHandler(
            GetPaginatedResultSetRequest(
                token = req.token!!,
                tableName = "sc.partners",
                filter = "order by id",
                getList = false,
                joinColumns = columnsNew,
                joinTables = joinString,
                columns = arrayOf(),
                id = req.id
            )
        ).result

        while (jdbcResult!!.next()){
            val financial_reporting_types: Array<out Any>? = (jdbcResult.getObject("financial_reporting_types") as? SerialArray)?.array as Array<out Any>?
            partner = Partner(
                id = jdbcResult.getString("id"),
                active = jdbcResult.getBoolean("active"),
                financial_reporting_types = financial_reporting_types,
                is_innovations_client = jdbcResult.getBoolean("is_innovations_client"),
                pmc_entity_code = jdbcResult.getString("pmc_entity_code"),
                point_of_contact = jdbcResult.getString("point_of_contact"),
                types = jdbcResult.getString("types"),
                address = jdbcResult.getString("address"),
                partner_sensitivity = jdbcResult.getString("partner_sensitivity"),
                name = jdbcResult.getString("name"),
                sensitivity = jdbcResult.getString("sensitivity"),
                primary_location = jdbcResult.getString("primary_location"),
                gpaid = jdbcResult.getString("gpaid"),
                gppid = jdbcResult.getString("gppid"),
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
                engagements = jdbcResult.getString("id")?.let { this.getEngagements(it, req.token!!) }
            )
        }
        return PartnerReadResponse(error = error, partner = partner)
    }

    fun getEngagements(partner: String, token: String): MutableList<GlobalPartnerEngagement>{
        var data: MutableList<GlobalPartnerEngagement> = mutableListOf()
        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", token)
        val queryRes = secureResultSet.getPaginatedResultSetHandler(
            GetPaginatedResultSetRequest(
                tableName = "sc.global_partner_engagements",
                token = token,
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
        )

        val jdbcResult = queryRes.result
        while (jdbcResult!!.next()){
            val sc_roles = (jdbcResult.getObject("sc_roles") as SerialArray).array as Array<out Any>
            val partner_roles = (jdbcResult.getObject("partner_roles") as SerialArray).array as Array<out Any>
            data.add(
                GlobalPartnerEngagement(
                    id = jdbcResult.getString("id"),
                    organization = jdbcResult.getString("organization"),
                    type = jdbcResult.getString("type"),
                    mou_start = jdbcResult.getString("mou_start"),
                    mou_end = jdbcResult.getString("mou_end"),
                    sc_roles = sc_roles,
                    partner_roles = partner_roles,
                    created_at = jdbcResult.getString("created_at")
                )
            )
        }
        return data
    }



  /*
    @CrossOrigin(origins =  ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
    @PostMapping("sc/partners-crm/generate-data")
    @ResponseBody
    fun generatePartnersData(){
        val token = ""
        val sql = """
            SELECT * FROM common.organizations WHERE id NOT IN(SELECT id FROM sc.partners) OFFSET 0 LIMIT 25000;
        """.trimIndent()
        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", token)
        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(sql, paramSource)
            while (jdbcResult.next()){
                var id = jdbcResult.getString("id")
                var insertId = jdbcTemplate2.queryForObject("""
                    INSERT INTO sc.partners (id, active, financial_reporting_types, is_innovations_client, pmc_entity_code, point_of_contact, types, address, sensitivity,
                     created_at, created_by, modified_at, modified_by, owning_person, owning_group) VALUES (
                        '${jdbcResult.getString("id")}'::uuid,
                        (round(random())::int)::boolean, 
                        ARRAY[(ARRAY['Funded','FieldEngaged'])[floor(random() * 2 + 1)::int]]::sc.financial_reporting_types[],
                        (round(random())::int)::boolean, 
                        SUBSTR(md5(random()::text),1,8),
                        '452aa149-05aa-4b32-ad47-d732f673d3e2'::uuid,
                        ARRAY[(ARRAY['Managing','Funding','Impact','Technical','Resource'])[floor(random() * 5 + 1)::int]]::sc.partner_types[],
                        md5(random()::text),
                        ((ARRAY['Low','Medium','High'])[floor(random() * 3 + 1)::int])::common.sensitivity,
                        now(),
                        '452aa149-05aa-4b32-ad47-d732f673d3e2'::uuid,
                        now(),
                        '452aa149-05aa-4b32-ad47-d732f673d3e2'::uuid,
                        '452aa149-05aa-4b32-ad47-d732f673d3e2'::uuid,
                        '2cbce651-1a69-4a98-a04a-ff7a3a3d07fd'::uuid
                    ) returning id;
                """.trimIndent(),
                  String::class.java)

                println("INSERTED partner $insertId")
            }
        }
        catch (e: SQLException){

        }
    }
  */

}
