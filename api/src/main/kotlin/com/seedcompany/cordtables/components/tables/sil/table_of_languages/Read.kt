package com.seedcompany.cordtables.components.tables.sil.table_of_languages

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

data class SilTableOfLanguagesReadRequest(
    val token: String?,
    val id: String? = null,
)

data class SilTableOfLanguagesReadResponse(
    val error: ErrorType,
    val tableOfLanguage: tableOfLanguage? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilTableOfLanguagesRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/table-of-languages/read")
    @ResponseBody
    fun readHandler(@RequestBody req: SilTableOfLanguagesReadRequest): SilTableOfLanguagesReadResponse {

        if (req.token == null) return SilTableOfLanguagesReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return SilTableOfLanguagesReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.table_of_languages",
                getList = false,
                columns = arrayOf(
                    "id",
                    "iso_639",
                    "language_name",
                    "uninverted_name",
                    "country_code",
                    "country_name",
                    "region_code",
                    "region_name",
                    "area",
                    "l1_users",
                    "digits",
                    "all_users",
                    "countries",
                    "family",
                    "classification",
                    "latitude",
                    "longitude",
                    "egids",
                    "is_written",
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

                var iso_639: String? = jdbcResult.getString("iso_639")
                if (jdbcResult.wasNull()) iso_639 = null

                var language_name: String? = jdbcResult.getString("language_name")
                if (jdbcResult.wasNull()) language_name = null

                var uninverted_name: String? = jdbcResult.getString("uninverted_name")
                if (jdbcResult.wasNull()) uninverted_name = null

                var country_code: String? = jdbcResult.getString("country_code")
                if (jdbcResult.wasNull()) country_code = null

                var country_name: String? = jdbcResult.getString("country_name")
                if (jdbcResult.wasNull()) country_name = null

                var region_code: String? = jdbcResult.getString("region_code")
                if (jdbcResult.wasNull()) region_code = null

                var region_name: String? = jdbcResult.getString("region_name")
                if (jdbcResult.wasNull()) region_name = null

                var area: String? = jdbcResult.getString("area")
                if (jdbcResult.wasNull()) area = null

                var l1_users: Int? = jdbcResult.getInt("l1_users")
                if (jdbcResult.wasNull()) l1_users = null

                var digits: Int? = jdbcResult.getInt("digits")
                if (jdbcResult.wasNull()) digits = null

                var all_users: Int? = jdbcResult.getInt("all_users")
                if (jdbcResult.wasNull()) all_users = null

                var countries: Int? = jdbcResult.getInt("countries")
                if (jdbcResult.wasNull()) countries = null

                var family: String? = jdbcResult.getString("family")
                if (jdbcResult.wasNull()) family = null

                var classification: String? = jdbcResult.getString("classification")
                if (jdbcResult.wasNull()) classification = null

                var latitude: Double? = jdbcResult.getDouble("latitude")
                if (jdbcResult.wasNull()) latitude = null

                var longitude: Double? = jdbcResult.getDouble("longitude")
                if (jdbcResult.wasNull()) longitude = null

                var egids: String? = jdbcResult.getString("egids")
                if (jdbcResult.wasNull()) egids = null

                var is_written: String? = jdbcResult.getString("is_written")
                if (jdbcResult.wasNull()) is_written = null

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

                val tableOfLanguage =
                    tableOfLanguage(
                        id = id,
                        iso_639 = iso_639,
                        language_name = language_name,
                        uninverted_name = uninverted_name,
                        country_code = country_code,
                        country_name = country_name,
                        region_code = region_code,
                        region_name = region_name,
                        area = area,
                        l1_users = l1_users,
                        digits = digits,
                        all_users = all_users,
                        countries = countries,
                        family = family,
                        classification = classification,
                        latitude = latitude,
                        longitude = longitude,
                        egids = egids,
                        is_written = is_written,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return SilTableOfLanguagesReadResponse(ErrorType.NoError, tableOfLanguage = tableOfLanguage)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilTableOfLanguagesReadResponse(ErrorType.SQLReadError)
        }

        return SilTableOfLanguagesReadResponse(error = ErrorType.UnknownError)
    }
}
