package com.seedcompany.cordtables.components.tables.sc.locations

import com.seedcompany.cordtables.common.LocationType
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

data class ScLocationsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScLocationsReadResponse(
    val error: ErrorType,
    val location: ScLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLocationsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/locations/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScLocationsReadRequest): ScLocationsReadResponse {

        if (req.token == null) return ScLocationsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScLocationsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.locations",
                getList = false,
                columns = arrayOf(
                    "id",
                    "neo4j_id",
                    "default_region",
                    "funding_account",
                    "iso_alpha_3",
                    "name",
                    "type",
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

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var neo4j_id: String? = jdbcResult.getString("neo4j_id")
                if (jdbcResult.wasNull()) neo4j_id = null

                var defaultRegion: String? = jdbcResult.getString("default_region")
                if (jdbcResult.wasNull()) defaultRegion = null

                var fundingAccount: String? = jdbcResult.getString("funding_account")
                if (jdbcResult.wasNull()) fundingAccount = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

                var isoAlpha3: String? = jdbcResult.getString("iso_alpha_3")
                if (jdbcResult.wasNull()) isoAlpha3 = null

                var type: String? = jdbcResult.getString("type")
                if (jdbcResult.wasNull()) type = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val location =
                    ScLocation(
                        id = id,
                        neo4j_id = neo4j_id,
                        default_region = defaultRegion,
                        name = name,
                        type = if (type == null) null else LocationType.valueOf(type),
                        funding_account = fundingAccount,
                        iso_alpha_3 = isoAlpha3,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group
                    )

                return ScLocationsReadResponse(ErrorType.NoError, location = location)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScLocationsReadResponse(ErrorType.SQLReadError)
        }

        return ScLocationsReadResponse(error = ErrorType.UnknownError)
    }
}
