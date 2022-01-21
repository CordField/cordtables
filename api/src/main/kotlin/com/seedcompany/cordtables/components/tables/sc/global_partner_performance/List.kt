package com.seedcompany.cordtables.components.tables.sc.global_partner_performance

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
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


data class ScGlobalPartnerPerformanceListRequest(
    val token: String?
)

data class ScGlobalPartnerPerformanceListResponse(
    val error: ErrorType,
    val globalPartnerPerformances: MutableList<globalPartnerPerformance>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerPerformanceList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/global-partner-performance/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScGlobalPartnerPerformanceListRequest): ScGlobalPartnerPerformanceListResponse {
        var data: MutableList<globalPartnerPerformance> = mutableListOf()
        if (req.token == null) return ScGlobalPartnerPerformanceListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.global_partner_performance",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "organization",
                    "reporting_performance",
                    "financial_performance",
                    "translation_performance",
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

                var organization: String? = jdbcResult.getString("organization")
                if (jdbcResult.wasNull()) organization = null

                var reporting_performance: String? = jdbcResult.getString("reporting_performance")
                if (jdbcResult.wasNull()) reporting_performance = null

                var financial_performance: String? = jdbcResult.getString("financial_performance")
                if (jdbcResult.wasNull()) financial_performance = null

                var translation_performance: String? = jdbcResult.getString("translation_performance")
                if (jdbcResult.wasNull()) translation_performance = null

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
                    globalPartnerPerformance(
                        id = id,
                        organization = organization,
                        reporting_performance = if (reporting_performance == null) null else PartnerPerformanceOptions.valueOf(reporting_performance),
                        financial_performance = if (financial_performance == null) null else PartnerPerformanceOptions.valueOf(financial_performance),
                        translation_performance = if (translation_performance == null) null else PartnerPerformanceOptions.valueOf(translation_performance),
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
            return ScGlobalPartnerPerformanceListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScGlobalPartnerPerformanceListResponse(ErrorType.NoError, data)
    }
}
