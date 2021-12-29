package com.seedcompany.cordtables.components.tables.sil.country_codes

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.GetPaginatedResultSet
import com.seedcompany.cordtables.common.GetPaginatedResultSetRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource


data class SilCountryCodesListRequest(
    val token: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilCountryCodesListResponse(
    val error: ErrorType,
    val size: Int,
    val countryCodes: MutableList<countryCode>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilCountryCodesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetPaginatedResultSet,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/country-codes/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilCountryCodesListRequest): SilCountryCodesListResponse {
        var data: MutableList<countryCode> = mutableListOf()
        if (req.token == null) return SilCountryCodesListResponse(ErrorType.TokenNotFound, size = 0, mutableListOf())

        val jdbcResult = secureList.getPaginatedResultSetHandler(
            GetPaginatedResultSetRequest(
                tableName = "sil.country_codes",
                token = req.token,
                page = req.page!!,
                resultsPerPage = req.resultsPerPage!!,
                filter = "order by id",
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
                )
            )
        )

        val resultSet = jdbcResult.result
        val size = jdbcResult.size
        if (jdbcResult.errorType == ErrorType.NoError){
            while (resultSet!!.next()) {
                var id: Int? = resultSet!!.getInt("id")
                if (resultSet!!.wasNull()) id = null

                var country: String? = resultSet!!.getString("country")
                if (resultSet!!.wasNull()) country = null

                var name: String? = resultSet!!.getString("name")
                if (resultSet!!.wasNull()) name = null

                var area: String? = resultSet!!.getString("area")
                if (resultSet!!.wasNull()) area = null

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
                )
            }
        }
        else{
          return SilCountryCodesListResponse(ErrorType.SQLReadError, size=0, mutableListOf())
        }
        return SilCountryCodesListResponse(ErrorType.NoError, size = size,  data)
    }
}

