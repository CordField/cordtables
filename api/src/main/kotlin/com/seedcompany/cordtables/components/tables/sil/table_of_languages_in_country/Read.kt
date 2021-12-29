package com.seedcompany.cordtables.components.tables.sil.table_of_languages_in_country

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.table_of_languages_in_country.tableOfLanguagesInCountry
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

data class SilTableOfLanguagesInCountryReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class SilTableOfLanguagesInCountryReadResponse(
    val error: ErrorType,
    val tableOfLanguagesInCountry: tableOfLanguagesInCountry? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilTableOfLanguagesInCountryRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/table-of-languages-in-country/read")
    @ResponseBody
    fun readHandler(@RequestBody req: SilTableOfLanguagesInCountryReadRequest): SilTableOfLanguagesInCountryReadResponse {

        if (req.token == null) return SilTableOfLanguagesInCountryReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return SilTableOfLanguagesInCountryReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.table_of_languages_in_country",
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
                    "is_primary",
                    "is_indigenous",
                    "is_established",
                    "all_users",
                    "l1_users",
                    "l2_users",
                    "family",
                    "egids",
                    "function_code",
                    "function_label",
                    "institutional",
                    "developing",
                    "vigorous",
                    "in_trouble",
                    "dying",
                    "extinct",
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

                var is_primary: String? = jdbcResult.getString("is_primary")
                if (jdbcResult.wasNull()) is_primary = null

                var is_indigenous: String? = jdbcResult.getString("is_indigenous")
                if (jdbcResult.wasNull()) is_indigenous = null

                var is_established: String? = jdbcResult.getString("is_established")
                if (jdbcResult.wasNull()) is_established = null


                var all_users: Int? = jdbcResult.getInt("all_users")
                if (jdbcResult.wasNull()) all_users = null

                var l1_users: Int? = jdbcResult.getInt("l1_users")
                if (jdbcResult.wasNull()) l1_users = null

                var l2_users: Int? = jdbcResult.getInt("l2_users")
                if (jdbcResult.wasNull()) l2_users = null


                var family: String? = jdbcResult.getString("family")
                if (jdbcResult.wasNull()) family = null

                var egids: String? = jdbcResult.getString("egids")
                if (jdbcResult.wasNull()) egids = null

                var function_code: String? = jdbcResult.getString("function_code")
                if (jdbcResult.wasNull()) function_code = null

                var function_label: String? = jdbcResult.getString("function_label")
                if (jdbcResult.wasNull()) function_label = null

                var institutional: Int? = jdbcResult.getInt("institutional")
                if (jdbcResult.wasNull()) institutional = null

                var developing: Int? = jdbcResult.getInt("developing")
                if (jdbcResult.wasNull()) developing = null

                var vigorous: Int? = jdbcResult.getInt("vigorous")
                if (jdbcResult.wasNull()) vigorous = null

                var in_trouble: Int? = jdbcResult.getInt("in_trouble")
                if (jdbcResult.wasNull()) in_trouble = null

                var dying: Int? = jdbcResult.getInt("dying")
                if (jdbcResult.wasNull()) dying = null

                var extinct: Int? = jdbcResult.getInt("extinct")
                if (jdbcResult.wasNull()) extinct = null

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

                val tableOfLanguagesInCountry =
                    tableOfLanguagesInCountry(
                        id = id,
                        iso_639 = iso_639,
                        language_name = language_name,
                        uninverted_name = uninverted_name,
                        country_code = country_code,
                        country_name = country_name,
                        region_code = region_code,
                        region_name = region_name,
                        area = area,
                        is_primary = is_primary,
                        is_indigenous = is_indigenous,
                        is_established = is_established,
                        all_users = all_users,
                        l1_users = l1_users,
                        l2_users = l2_users,
                        family = family,
                        egids = egids,
                        function_code = function_code,
                        function_label = function_label,
                        institutional = institutional,
                        developing = developing,
                        vigorous = vigorous,
                        in_trouble = in_trouble,
                        dying = dying,
                        extinct = extinct,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return SilTableOfLanguagesInCountryReadResponse(ErrorType.NoError, tableOfLanguagesInCountry = tableOfLanguagesInCountry)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilTableOfLanguagesInCountryReadResponse(ErrorType.SQLReadError)
        }

        return SilTableOfLanguagesInCountryReadResponse(error = ErrorType.UnknownError)
    }
}
