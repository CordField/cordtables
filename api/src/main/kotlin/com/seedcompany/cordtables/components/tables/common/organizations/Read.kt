package com.seedcompany.cordtables.components.tables.common.organizations

import com.seedcompany.cordtables.components.tables.common.organizations.*

import com.seedcompany.cordtables.common.CommonSensitivity
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

data class CommonOrganizationsReadRequest(
        val token: String?,
        val id: Int? = null,
)

data class CommonOrganizationsReadResponse(
        val error: ErrorType,
        val organization: CommonOrganization? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonOrganizationsRead")
class Read(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-organizations/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonOrganizationsReadRequest): CommonOrganizationsReadResponse {

        if (req.token == null) return CommonOrganizationsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonOrganizationsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
                GetSecureListQueryRequest(
                        tableName = "common.organizations",
                        getList = false,
                        columns = arrayOf(
                                "id",
                                "name",
                                "sensitivity",
                                "primary_location",
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

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

                var sensitivity: String? = jdbcResult.getString("sensitivity")
                if (jdbcResult.wasNull()) sensitivity = null

                var primary_location: Int? = jdbcResult.getInt("primary_location")
                if (jdbcResult.wasNull()) primary_location = null

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

                val organization =
                        CommonOrganization(
                                id = id,
                                name = name,
                                sensitivity = if (sensitivity == null) null else CommonSensitivity.valueOf(sensitivity),
                                primary_location = primary_location,
                                created_at = created_at,
                                created_by = created_by,
                                modified_at = modified_at,
                                modified_by = modified_by,
                                owning_person = owning_person,
                                owning_group = owning_group
                        )

                return CommonOrganizationsReadResponse(ErrorType.NoError, organization = organization)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonOrganizationsReadResponse(ErrorType.SQLReadError)
        }

        return CommonOrganizationsReadResponse(error = ErrorType.UnknownError)
    }
}