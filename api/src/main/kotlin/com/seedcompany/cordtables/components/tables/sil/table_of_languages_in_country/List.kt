package com.seedcompany.cordtables.components.tables.sil.table_of_languages_in_country

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


data class SilTableOfLanguagesInCountryListRequest(
    val token: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilTableOfLanguagesInCountryListResponse(
    val error: ErrorType,
    val size: Int,
    val tableOfLanguagesInCountries: MutableList<tableOfLanguagesInCountry>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilTableOfLanguagesInCountryList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetPaginatedResultSet,
) {

//    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/table-of-languages-in-country/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilTableOfLanguagesInCountryListRequest): SilTableOfLanguagesInCountryListResponse {
        var data: MutableList<tableOfLanguagesInCountry> = mutableListOf()
        if (req.token == null) return SilTableOfLanguagesInCountryListResponse(ErrorType.TokenNotFound, size = 0, mutableListOf())

//        val paramSource = MapSqlParameterSource()
//        paramSource.addValue("token", req.token)

        val jdbcResult = secureList.getPaginatedResultSetHandler(
            GetPaginatedResultSetRequest(
                tableName = "sil.table_of_languages_in_country",
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
                )
            )
        )

        val resultSet = jdbcResult.result
        val size = jdbcResult.size
        if (jdbcResult.errorType == ErrorType.NoError){
            while (resultSet!!.next()) {
                var id: Int? = resultSet!!.getInt("id")
                if (resultSet!!.wasNull()) id = null

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

                var is_primary: String? = resultSet!!.getString("is_primary")
                if (resultSet!!.wasNull()) is_primary = null

                var is_indigenous: String? = resultSet!!.getString("is_indigenous")
                if (resultSet!!.wasNull()) is_indigenous = null

                var is_established: String? = resultSet!!.getString("is_established")
                if (resultSet!!.wasNull()) is_established = null

                var all_users: Int? = resultSet!!.getInt("all_users")
                if (resultSet!!.wasNull()) all_users = null

                var l1_users: Int? = resultSet!!.getInt("l1_users")
                if (resultSet!!.wasNull()) l1_users = null

                var l2_users: Int? = resultSet!!.getInt("l2_users")
                if (resultSet!!.wasNull()) l2_users = null

                var family: String? = resultSet!!.getString("family")
                if (resultSet!!.wasNull()) family = null

                var egids: String? = resultSet!!.getString("egids")
                if (resultSet!!.wasNull()) egids = null

                var function_code: String? = resultSet!!.getString("function_code")
                if (resultSet!!.wasNull()) function_code = null

                var function_label: String? = resultSet!!.getString("function_label")
                if (resultSet!!.wasNull()) function_label = null

                var institutional: Int? = resultSet!!.getInt("institutional")
                if (resultSet!!.wasNull()) institutional = null

                var developing: Int? = resultSet!!.getInt("developing")
                if (resultSet!!.wasNull()) developing = null

                var vigorous: Int? = resultSet!!.getInt("vigorous")
                if (resultSet!!.wasNull()) vigorous = null

                var in_trouble: Int? = resultSet!!.getInt("in_trouble")
                if (resultSet!!.wasNull()) in_trouble = null

                var dying: Int? = resultSet!!.getInt("dying")
                if (resultSet!!.wasNull()) dying = null

                var extinct: Int? = resultSet!!.getInt("extinct")
                if (resultSet!!.wasNull()) extinct = null

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
                )
            }
        }
        else{
            return SilTableOfLanguagesInCountryListResponse(ErrorType.SQLReadError, size = 0, mutableListOf())
        }

        return SilTableOfLanguagesInCountryListResponse(ErrorType.NoError, size = size, data)
    }
}

