package com.seedcompany.cordtables.components.tables.sil.country_codes

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.country_codes.countryCode
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

data class SilCountryCodesReadRequest(
    val token: String?,
    val id: String? = null,
)

data class SilCountryCodesReadResponse(
    val error: ErrorType,
    val countryCode: countryCode? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilCountryCodesRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/country-codes/read")
    @ResponseBody
    fun readHandler(@RequestBody req: SilCountryCodesReadRequest): SilCountryCodesReadResponse {

        if (req.token == null) return SilCountryCodesReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return SilCountryCodesReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.country_codes",
                getList = false,
                columns = arrayOf(
                    "id",
                    "country",
                    "name",
                    "area",
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

                var country: String? = jdbcResult.getString("country")
                if (jdbcResult.wasNull()) country = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

                var area: String? = jdbcResult.getString("area")
                if (jdbcResult.wasNull()) area = null

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

                val countryCode =
                    countryCode(
                        id = id,
                        country = country,
                        name = name,
                        area = area,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return SilCountryCodesReadResponse(ErrorType.NoError, countryCode = countryCode)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilCountryCodesReadResponse(ErrorType.SQLReadError)
        }

        return SilCountryCodesReadResponse(error = ErrorType.UnknownError)
    }
}
