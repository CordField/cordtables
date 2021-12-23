package com.seedcompany.cordtables.components.tables.sil.table_of_languages

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.GetPaginatedResultSet
import com.seedcompany.cordtables.common.GetPaginatedResultSetRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class SilTableOfLanguagesListRequest(
    val token: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilTableOfLanguagesListResponse(
    val error: ErrorType,
    val size: Int,
    val tableOfLanguages: MutableList<tableOfLanguage>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilTableOfLanguagesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetPaginatedResultSet,
) {

//    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/table-of-languages/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilTableOfLanguagesListRequest): SilTableOfLanguagesListResponse {
        var data: MutableList<tableOfLanguage> = mutableListOf()
        if (req.token == null) return SilTableOfLanguagesListResponse(ErrorType.TokenNotFound, size = 0, mutableListOf())

//        val paramSource = MapSqlParameterSource()
//        paramSource.addValue("token", req.token)

        val jdbcResult = secureList.getPaginatedResultSetHandler(
            GetPaginatedResultSetRequest(
                tableName = "sil.table_of_languages",
                filter = "order by id",
                token = req.token,
                page = req.page!!,
                resultsPerPage = req.resultsPerPage!!,
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
                )
            )
        )

        val resultSet = jdbcResult.result
        val size = jdbcResult.size
        if (jdbcResult.errorType == ErrorType.NoError){
            while (resultSet!!.next()) {
              
                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var iso_639: String? = resultSet!!.getString("iso_639")
                if (resultSet!!.wasNull()) iso_639 = null

                var language_name: String? = resultSet!!.getString("language_name")
                if (resultSet!!.wasNull()) language_name = null

                var uninverted_name: String? = resultSet!!.getString("uninverted_name")
                if (resultSet!!.wasNull()) uninverted_name = null

                var country_code: String? = resultSet!!.getString("country_code")
                if (resultSet!!.wasNull()) country_code = null

                var country_name: String? = resultSet!!.getString("country_name")
                if (resultSet!!.wasNull()) country_name = null

                var region_code: String? = resultSet!!.getString("region_code")
                if (resultSet!!.wasNull()) region_code = null

                var region_name: String? = resultSet!!.getString("region_name")
                if (resultSet!!.wasNull()) region_name = null

                var area: String? = resultSet!!.getString("area")
                if (resultSet!!.wasNull()) area = null

                var l1_users: Int? = resultSet!!.getInt("l1_users")
                if (resultSet!!.wasNull()) l1_users = null

                var digits: Int? = resultSet!!.getInt("digits")
                if (resultSet!!.wasNull()) digits = null

                var all_users: Int? = resultSet!!.getInt("all_users")
                if (resultSet!!.wasNull()) all_users = null

                var countries: Int? = resultSet!!.getInt("countries")
                if (resultSet!!.wasNull()) countries = null

                var family: String? = resultSet!!.getString("family")
                if (resultSet!!.wasNull()) family = null

                var classification: String? = resultSet!!.getString("classification")
                if (resultSet!!.wasNull()) classification = null

                var latitude: Double? = resultSet!!.getDouble("latitude")
                if (resultSet!!.wasNull()) latitude = null

                var longitude: Double? = resultSet!!.getDouble("longitude")
                if (resultSet!!.wasNull()) longitude = null

                var egids: String? = resultSet!!.getString("egids")
                if (resultSet!!.wasNull()) egids = null

                var is_written: String? = resultSet!!.getString("is_written")
                if (resultSet!!.wasNull()) is_written = null

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
                )
            }
        }
        else{
            return SilTableOfLanguagesListResponse(ErrorType.SQLReadError, size = 0, mutableListOf())
        }

        return SilTableOfLanguagesListResponse(ErrorType.NoError, size = size, data)
    }
}

