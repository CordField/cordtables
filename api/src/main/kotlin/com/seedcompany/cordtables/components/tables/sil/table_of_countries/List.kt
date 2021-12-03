package com.seedcompany.cordtables.components.tables.sil.table_of_countries

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.table_of_countries.tableOfCountry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import software.amazon.ion.Decimal
import java.math.BigDecimal
import java.sql.SQLException
import javax.sql.DataSource


data class SilTableOfCountriesListRequest(
    val token: String?
)

data class SilTableOfCountriesListResponse(
    val error: ErrorType,
    val tableOfCountries: MutableList<tableOfCountry>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilTableOfCountriesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil-table-of-countries/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilTableOfCountriesListRequest): SilTableOfCountriesListResponse {
        var data: MutableList<tableOfCountry> = mutableListOf()
        if (req.token == null) return SilTableOfCountriesListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.table_of_countries",
                filter = "order by id",
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
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
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
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilTableOfCountriesListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return SilTableOfCountriesListResponse(ErrorType.NoError, data)
    }
}

