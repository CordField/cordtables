package com.seedcompany.cordtables.components.tables.sil.iso_639_3_names

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


data class SilIso6393NamesListRequest(
    val token: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilIso6393NamesListResponse(
    val error: ErrorType,
    val size: Int,
    val iso6393Names: MutableList<iso6393Name>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilIso6393NamesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureQuery,
) {

//    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil-iso-639-3-names/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilIso6393NamesListRequest): SilIso6393NamesListResponse {
        var data: MutableList<iso6393Name> = mutableListOf()
        if (req.token == null) return SilIso6393NamesListResponse(ErrorType.TokenNotFound, size=0, mutableListOf())

//        val paramSource = MapSqlParameterSource()
//        paramSource.addValue("token", req.token)

        val jdbcResult = secureList.getSecureQueryHandler(
            GetSecureQueryRequest(
                tableName = "sil.iso_639_3_names",
                filter = "order by id",
                token = req.token,
                page = req.page!!,
                resultsPerPage = req.resultsPerPage!!,
                columns = arrayOf(
                    "id",
                    "_id",
                    "print_name",
                    "inverted_name",
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

                var _id: String? = resultSet!!.getString("_id")
                if (resultSet!!.wasNull()) _id = null

                var print_name: String? = resultSet!!.getString("print_name")
                if (resultSet!!.wasNull()) print_name = null

                var inverted_name: String? = resultSet!!.getString("inverted_name")
                if (resultSet!!.wasNull()) inverted_name = null

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
                    iso6393Name(
                        id = id,
                        _id = _id,
                        print_name = print_name,
                        inverted_name = inverted_name,
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
            return SilIso6393NamesListResponse(ErrorType.SQLReadError, size=0, mutableListOf())
        }

        return SilIso6393NamesListResponse(ErrorType.NoError, size = size, data)
    }
}

