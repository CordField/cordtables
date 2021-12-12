package com.seedcompany.cordtables.components.tables.sil.language_codes

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

data class SilLanguageCodesListRequest(
    val token: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilLanguageCodesListResponse(
    val error: ErrorType,
    val size: Int,
    val languageCodes: MutableList<languageCode>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilLanguageCodesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureQuery,
) {

//    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil-language-codes/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilLanguageCodesListRequest): SilLanguageCodesListResponse {
        var data: MutableList<languageCode> = mutableListOf()
        if (req.token == null) return SilLanguageCodesListResponse(ErrorType.TokenNotFound, size=0, mutableListOf())

//        val paramSource = MapSqlParameterSource()
//        paramSource.addValue("token", req.token)

        val jdbcResult = secureList.getSecureQueryHandler(
            GetSecureQueryRequest(
                tableName = "sil.language_codes",
                filter = "order by id",
                token = req.token,
                page = req.page!!,
                resultsPerPage = req.resultsPerPage!!,
                columns = arrayOf(
                    "id",
                    "lang",
                    "country",
                    "lang_status",
                    "name",
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

                var lang: String? = resultSet!!.getString("lang")
                if (resultSet!!.wasNull()) lang = null

                var country: String? = resultSet!!.getString("country")
                if (resultSet!!.wasNull()) country = null

                var lang_status: String? = resultSet!!.getString("lang_status")
                if (resultSet!!.wasNull()) lang_status = null

                var name: String? = resultSet!!.getString("name")
                if (resultSet!!.wasNull()) name = null

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
                    languageCode(
                        id = id,
                        lang = lang,
                        country = country,
                        lang_status = lang_status,
                        name = name,
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
            return SilLanguageCodesListResponse(ErrorType.SQLReadError, size = 0, mutableListOf())
        }
        return SilLanguageCodesListResponse(ErrorType.NoError, size = size, data)
    }
}

