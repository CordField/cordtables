package com.seedcompany.cordtables.components.tables.sc.global_partner_performance

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.globalPartnerPerformanceInput
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.Read
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerPerformanceCreateRequest(
    val token: String? = null,
    val globalPartnerPerformance: globalPartnerPerformanceInput,
)

data class ScGlobalPartnerPerformanceCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerPerformanceCreate")
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

    @PostMapping("sc/global-partner-performance/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScGlobalPartnerPerformanceCreateRequest): ScGlobalPartnerPerformanceCreateResponse {

        // if (req.globalPartnerPerformance.name == null) return ScGlobalPartnerPerformanceCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.global_partner_performance(organization, reporting_performance, financial_performance, translation_performance, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::sc.partner_performance_options,
                    ?::sc.partner_performance_options,
                    ?::sc.partner_performance_options,
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
            req.globalPartnerPerformance.organization,
            req.globalPartnerPerformance.reporting_performance,
            req.globalPartnerPerformance.financial_performance,
            req.globalPartnerPerformance.translation_performance,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

//        req.language.id = id

        return ScGlobalPartnerPerformanceCreateResponse(error = ErrorType.NoError, id = id)
    }

}
