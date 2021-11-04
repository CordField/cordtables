package com.seedcompany.cordtables.components.tables.sc.partners

import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.languages.AccessToScriptureInLwcScale
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

data class ScPartnersReadRequest(
        val token: String?,
        val id: Int? = null,
)

data class ScPartnersReadResponse(
        val error: ErrorType,
        val partner: Partner? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScPartnersRead")
class Read(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-partners/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScPartnersReadRequest): ScPartnersReadResponse {

        if (req.token == null) return ScPartnersReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScPartnersReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
                GetSecureListQueryRequest(
                        tableName = "sc.partners",
                        getList = false,
                        columns = arrayOf(
                                "id",
                                "organization",
                                "active",
                                "financial_reporting_types",
                                "is_innovations_client",
                                "pmc_entity_code",
                                "point_of_contact",
                                "types",
                                "created_at",
                                "created_by",
                                "modified_at",
                                "modified_by",
                                "owning_person",
                                "owning_group",
                                "peer"
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
                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null
//                var financial_reporting_types: Array<String>? = jdbcResult.getString("financial_reporting_types")
//                if (jdbcResult.wasNull()) financial_reporting_types = null
                var is_innovations_client: Boolean? = jdbcResult.getBoolean("is_innovations_client")
                if (jdbcResult.wasNull()) is_innovations_client = null
                var pmc_entity_code: String? = jdbcResult.getString("pmc_entity_code")
                if (jdbcResult.wasNull()) pmc_entity_code = null
                var point_of_contact: Int? = jdbcResult.getInt("point_of_contact")
                if (jdbcResult.wasNull()) point_of_contact = null
                var types: String? = jdbcResult.getString("types")
                if (jdbcResult.wasNull()) types = null
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
                var peer: Int? = jdbcResult.getInt("peer")
                if (jdbcResult.wasNull()) peer = null


                val partner =
                        Partner(
                                id = id,

                                organization = organization,

                                active = active,

//                                financial_reporting_types ,

                                is_innovations_client = is_innovations_client,

                                pmc_entity_code = pmc_entity_code,

                                point_of_contact = point_of_contact,

//                                types = if(types == null) null else PartnerTypes.valueOf(types),
                                created_at = created_at,

                                created_by = created_by,

                                modified_at = modified_at,

                                modified_by = modified_by,

                                owning_person = owning_person,

                                owning_group = owning_group,

                                peer = peer,


                                )

                return ScPartnersReadResponse(ErrorType.NoError, partner = partner)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScPartnersReadResponse(ErrorType.SQLReadError)
        }

        return ScPartnersReadResponse(error = ErrorType.UnknownError)
    }
}
