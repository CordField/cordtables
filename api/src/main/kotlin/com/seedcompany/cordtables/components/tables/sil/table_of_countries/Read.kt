package com.seedcompany.cordtables.components.tables.sil.table_of_countries

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.table_of_countries.tableOfCountry
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

data class SilTableOfCountriesReadRequest(
    val token: String?,
    val id: String? = null,
)

data class SilTableOfCountriesReadResponse(
    val error: ErrorType,
    val tableOfCountry: tableOfCountry? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilTableOfCountriesRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/table-of-countries/read")
    @ResponseBody
    fun readHandler(@RequestBody req: SilTableOfCountriesReadRequest): SilTableOfCountriesReadResponse {

        if (req.token == null) return SilTableOfCountriesReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return SilTableOfCountriesReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.table_of_countries",
                getList = false,
                columns = arrayOf(
                    "id",
                    "country_code",
                    "country_name",
                    "languages",
                    "indigenous",
                    "established",
                    "unestablished",
                    "diversity",
                    "included",
                    "sum_of_populations",
                    "mean",
                    "median",
                    "population",
                    "literacy_rate",
                    "conventions",
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

                var country_code: String? = jdbcResult.getString("country_code")
                if (jdbcResult.wasNull()) country_code = null

                var country_name: String? = jdbcResult.getString("country_name")
                if (jdbcResult.wasNull()) country_name = null

                var languages: Int? = jdbcResult.getInt("languages")
                if (jdbcResult.wasNull()) languages = null

                var indigenous: Int? = jdbcResult.getInt("indigenous")
                if (jdbcResult.wasNull()) indigenous = null

                var established: Int? = jdbcResult.getInt("established")
                if (jdbcResult.wasNull()) established = null

                var unestablished: Int? = jdbcResult.getInt("unestablished")
                if (jdbcResult.wasNull()) unestablished = null

                var diversity: Float? = jdbcResult.getFloat("diversity")
                if (jdbcResult.wasNull()) diversity = null

                var included: Int? = jdbcResult.getInt("included")
                if (jdbcResult.wasNull()) included = null

                var sum_of_populations: Int? = jdbcResult.getInt("sum_of_populations")
                if (jdbcResult.wasNull()) sum_of_populations = null

                var mean: Int? = jdbcResult.getInt("mean")
                if (jdbcResult.wasNull()) mean = null

                var median: Int? = jdbcResult.getInt("median")
                if (jdbcResult.wasNull()) median = null

                var population: Int? = jdbcResult.getInt("population")
                if (jdbcResult.wasNull()) population = null


                var literacy_rate: Float? = jdbcResult.getFloat("literacy_rate")
                if (jdbcResult.wasNull()) literacy_rate = null

                var conventions: Int? = jdbcResult.getInt("conventions")
                if (jdbcResult.wasNull()) conventions = null

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

                val tableOfCountry =
                    tableOfCountry(
                        id = id,
                        country_code = country_code,
                        country_name = country_name,
                        languages = languages,
                        indigenous = indigenous,
                        established = established,
                        unestablished = unestablished,
                        diversity = diversity,
                        included = included,
                        sum_of_populations = sum_of_populations,
                        mean = mean,
                        median = median,
                        population = population,
                        literacy_rate = literacy_rate,
                        conventions = conventions,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return SilTableOfCountriesReadResponse(ErrorType.NoError, tableOfCountry = tableOfCountry)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilTableOfCountriesReadResponse(ErrorType.SQLReadError)
        }

        return SilTableOfCountriesReadResponse(error = ErrorType.UnknownError)
    }
}
