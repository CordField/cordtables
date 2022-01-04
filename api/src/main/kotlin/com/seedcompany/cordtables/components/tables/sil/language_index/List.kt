package com.seedcompany.cordtables.components.tables.sil.language_index

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

data class SilLanguageIndexListRequest(
    val token: String?,
    val search: String?,
    val page: Int? = 1,
    val resultsPerPage: Int? = 50
)

data class SilLanguageIndexListResponse(
    val error: ErrorType,
    val size: Int,
    val languageIndexes: MutableList<languageIndex>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilLanguageIndexList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetPaginatedResultSet,
) {

//    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil/language-index/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilLanguageIndexListRequest): SilLanguageIndexListResponse {
        var data: MutableList<languageIndex> = mutableListOf()
        if (req.token == null) return SilLanguageIndexListResponse(ErrorType.TokenNotFound, size = 0, mutableListOf())

        var whereClause = ""
        if(req.search != null && req.search != "") {
          val search = req.search.lowercase()
          whereClause = "lower(name) like '%${search}%'"
        }

        val jdbcResult = secureList.getPaginatedResultSetHandler(
            GetPaginatedResultSetRequest(
                tableName = "sil.language_index",
                whereClause = whereClause,
                filter = "order by id",
                token = req.token,
                page = req.page!!,
                resultsPerPage = req.resultsPerPage!!,
                columns = arrayOf(
                    "id",
                    "lang",
                    "country",
                    "name_type",
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
              
                var id: String? = resultSet.getString("id")
                if (resultSet.wasNull()) id = null

                var lang: String? = resultSet.getString("lang")
                if (resultSet.wasNull()) lang = null

                var country: String? = resultSet!!.getString("country")
                if (resultSet!!.wasNull()) country = null

                var name_type: String? = resultSet!!.getString("name_type")
                if (resultSet!!.wasNull()) name_type = null

                var name: String? = resultSet!!.getString("name")
                if (resultSet!!.wasNull()) name = null

                var created_at: String? = resultSet!!.getString("created_at")
                if (resultSet!!.wasNull()) created_at = null

                var created_by: String? = resultSet.getString("created_by")
                if (resultSet.wasNull()) created_by = null

                var modified_at: String? = resultSet!!.getString("modified_at")
                if (resultSet!!.wasNull()) modified_at = null
                
                var modified_by: String? = resultSet.getString("modified_by")
                if (resultSet.wasNull()) modified_by = null

                var owning_person: String? = resultSet.getString("owning_person")
                if (resultSet.wasNull()) owning_person = null

                var owning_group: String? = resultSet.getString("owning_group")
                if (resultSet.wasNull()) owning_group = null
                
                data.add(
                    languageIndex(
                        id = id,
                        lang = lang,
                        country = country,
                        name_type = name_type,
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
            return SilLanguageIndexListResponse(ErrorType.SQLReadError, size = 0, mutableListOf())
        }
        return SilLanguageIndexListResponse(ErrorType.NoError, size = size, data)
    }
}

