package com.seedcompany.cordtables.components.tables.sc.organization_locations

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.organization_locations.organizationLocation
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

data class ScOrganizationLocationsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScOrganizationLocationsReadResponse(
    val error: ErrorType,
    val organizationLocation: organizationLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScOrganizationLocationsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-organization-locations/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScOrganizationLocationsReadRequest): ScOrganizationLocationsReadResponse {

        if (req.token == null) return ScOrganizationLocationsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScOrganizationLocationsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.organization_locations",
                getList = false,
                columns = arrayOf(
                    "id",
                    "organization",
                    "location",
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

                var organization: Int? = jdbcResult.getInt("organization")
                if (jdbcResult.wasNull()) organization = null

                var location: Int? = jdbcResult.getInt("location")
                if (jdbcResult.wasNull()) location = null

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

                val organizationLocation =
                    organizationLocation(
                        id = id,
                        organization = organization,
                        location = location,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScOrganizationLocationsReadResponse(ErrorType.NoError, organizationLocation = organizationLocation)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScOrganizationLocationsReadResponse(ErrorType.SQLReadError)
        }

        return ScOrganizationLocationsReadResponse(error = ErrorType.UnknownError)
    }
}
