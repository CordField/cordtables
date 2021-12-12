package com.seedcompany.cordtables.components.tables.sil.table_of_countries

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.GetSecureQuery
import com.seedcompany.cordtables.common.GetSecureQueryRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class SilTableOfCountriesListRequest(
    val token: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilTableOfCountriesListResponse(
    val error: ErrorType,
    val size: Int,
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
    val secureList: GetSecureQuery,
) {

    // var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil-table-of-countries/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilTableOfCountriesListRequest): SilTableOfCountriesListResponse {
        var data: MutableList<tableOfCountry> = mutableListOf()
        if (req.token == null) return SilTableOfCountriesListResponse(ErrorType.TokenNotFound, size = 0, mutableListOf())

//        val paramSource = MapSqlParameterSource()
//        paramSource.addValue("token", req.token)

        val jdbcResult = secureList.getSecureQueryHandler(
            GetSecureQueryRequest(
                tableName = "sil.table_of_countries",
                filter = "order by id",
                token = req.token,
                page = req.page!!,
                resultsPerPage = req.resultsPerPage!!,
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
        )

        val resultSet = jdbcResult.result
        val size = jdbcResult.size
        if (jdbcResult.errorType == ErrorType.NoError){
            while (resultSet!!.next()) {

                var id: Int? = resultSet!!.getInt("id")
                if (resultSet!!.wasNull()) id = null

                var country_code: String? = resultSet!!.getString("country_code")
                if (resultSet!!.wasNull()) country_code = null

                var country_name: String? = resultSet!!.getString("country_name")
                if (resultSet!!.wasNull()) country_name = null

                var languages: Int? = resultSet!!.getInt("languages")
                if (resultSet!!.wasNull()) languages = null

                var indigenous: Int? = resultSet!!.getInt("indigenous")
                if (resultSet!!.wasNull()) indigenous = null

                var established: Int? = resultSet!!.getInt("established")
                if (resultSet!!.wasNull()) established = null

                var unestablished: Int? = resultSet!!.getInt("unestablished")
                if (resultSet!!.wasNull()) unestablished = null

                var diversity: Float? = resultSet!!.getFloat("diversity")
                if (resultSet!!.wasNull()) diversity = null

                var included: Int? = resultSet!!.getInt("included")
                if (resultSet!!.wasNull()) included = null

                var sum_of_populations: Int? = resultSet!!.getInt("sum_of_populations")
                if (resultSet!!.wasNull()) sum_of_populations = null

                var mean: Int? = resultSet!!.getInt("mean")
                if (resultSet!!.wasNull()) mean = null

                var median: Int? = resultSet!!.getInt("median")
                if (resultSet!!.wasNull()) median = null

                var population: Int? = resultSet!!.getInt("population")
                if (resultSet!!.wasNull()) population = null

                var literacy_rate: Float? = resultSet!!.getFloat("literacy_rate")
                if (resultSet!!.wasNull()) literacy_rate = null

                var conventions: Int? = resultSet!!.getInt("conventions")
                if (resultSet!!.wasNull()) conventions = null

                var created_by: Int? = resultSet!!.getInt("created_by")
                if (resultSet!!.wasNull()) created_by = null

                var created_at: String? = resultSet!!.getString("created_at")
                if (resultSet!!.wasNull()) created_at = null

                var modified_at: String? = resultSet!!.getString("modified_at")
                if (resultSet!!.wasNull()) modified_at = null

                var modified_by: Int? = resultSet!!.getInt("modified_by")
                if (resultSet!!.wasNull()) modified_by = null

                var owning_person: Int? = resultSet!!.getInt("owning_person")
                if (resultSet!!.wasNull()) owning_person = null

                var owning_group: Int? = resultSet!!.getInt("owning_group")
                if (resultSet!!.wasNull()) owning_group = null

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
        }
        else{
            return SilTableOfCountriesListResponse(ErrorType.SQLReadError, size = 0, mutableListOf())
        }

        return SilTableOfCountriesListResponse(ErrorType.NoError, size = size, data)
    }
}

