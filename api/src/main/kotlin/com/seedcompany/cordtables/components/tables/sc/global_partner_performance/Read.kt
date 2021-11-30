package com.seedcompany.cordtables.components.tables.sc.global_partner_performance

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_performance.globalPartnerPerformance
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocation
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

data class ScGlobalPartnerPerformanceReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class ScGlobalPartnerPerformanceReadResponse(
    val error: ErrorType,
    val globalPartnerPerformance: globalPartnerPerformance? = null,
)


@Controller("ScGlobalPartnerPerformanceRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-global-partner-performance/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScGlobalPartnerPerformanceReadRequest): ScGlobalPartnerPerformanceReadResponse {

        if (req.token == null) return ScGlobalPartnerPerformanceReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScGlobalPartnerPerformanceReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.global_partner_performance",
                getList = false,
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
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var organization: Int? = jdbcResult.getInt("organization")
                if (jdbcResult.wasNull()) organization = null

                var reporting_performance: String? = jdbcResult.getString("reporting_performance")
                if (jdbcResult.wasNull()) reporting_performance = null

                var financial_performance: String? = jdbcResult.getString("financial_performance")
                if (jdbcResult.wasNull()) financial_performance = null

                var translation_performance: String? = jdbcResult.getString("translation_performance")
                if (jdbcResult.wasNull()) translation_performance = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val globalPartnerPerformance =
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

                return ScGlobalPartnerPerformanceReadResponse(ErrorType.NoError, globalPartnerPerformance = globalPartnerPerformance)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScGlobalPartnerPerformanceReadResponse(ErrorType.SQLReadError)
        }

        return ScGlobalPartnerPerformanceReadResponse(error = ErrorType.UnknownError)
    }
}