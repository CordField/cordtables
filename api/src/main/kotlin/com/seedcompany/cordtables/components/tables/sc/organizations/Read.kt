package com.seedcompany.cordtables.components.tables.sc.organizations

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.organizations.organization
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

data class ScOrganizationsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScOrganizationsReadResponse(
    val error: ErrorType,
    val organization: organization? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScOrganizationsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/organizations/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScOrganizationsReadRequest): ScOrganizationsReadResponse {

        if (req.token == null) return ScOrganizationsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScOrganizationsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.organizations",
                getList = false,
                columns = arrayOf(
                    "id",
                    "sensitivity",
                    "root_directory",
                    "address",
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

                var sensitivity: String? = jdbcResult.getString("sensitivity")
                if (jdbcResult.wasNull()) sensitivity = null

                var root_directory: String? = jdbcResult.getString("root_directory")
                if (jdbcResult.wasNull()) root_directory = null

                var address: String? = jdbcResult.getString("address")
                if (jdbcResult.wasNull()) address = null

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

                val organization =
                    organization(
                        id = id,
                        sensitivity = sensitivity,
                        root_directory = root_directory,
                        address = address,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScOrganizationsReadResponse(ErrorType.NoError, organization = organization)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScOrganizationsReadResponse(ErrorType.SQLReadError)
        }

        return ScOrganizationsReadResponse(error = ErrorType.UnknownError)
    }
}
