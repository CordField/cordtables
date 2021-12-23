package com.seedcompany.cordtables.components.tables.sc.partners

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.partners.partner
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


data class ScPartnersListRequest(
    val token: String?
)

data class ScPartnersListResponse(
    val error: ErrorType,
    val partners: MutableList<partner>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPartnersList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-partners/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScPartnersListRequest): ScPartnersListResponse {
        var data: MutableList<partner> = mutableListOf()
        if (req.token == null) return ScPartnersListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.partners",
                filter = "order by id",
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
                )
            )
        ).query




        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null


                var organization: Int? = jdbcResult.getInt("organization")
                if (jdbcResult.wasNull()) organization = null

                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null

                var financial_reporting_types: String? = jdbcResult.getString("financial_reporting_types")
                if (jdbcResult.wasNull()) financial_reporting_types = null

                var is_innovations_client: Boolean? = jdbcResult.getBoolean("is_innovations_client")
                if (jdbcResult.wasNull()) is_innovations_client = null

                var pmc_entity_code: String? = jdbcResult.getString("pmc_entity_code")
                if (jdbcResult.wasNull()) pmc_entity_code = null

                var point_of_contact: Int? = jdbcResult.getInt("point_of_contact")
                if (jdbcResult.wasNull()) point_of_contact = null

                var types: String? = jdbcResult.getString("types")
                if (jdbcResult.wasNull()) types = null






                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    partner(
                        id = id,

                        organization = organization,
                        active = active,
                        financial_reporting_types = financial_reporting_types,
                        is_innovations_client = is_innovations_client,
                        pmc_entity_code = pmc_entity_code,
                        point_of_contact = point_of_contact,
                        types = types,

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
            return ScPartnersListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScPartnersListResponse(ErrorType.NoError, data)
    }
}

