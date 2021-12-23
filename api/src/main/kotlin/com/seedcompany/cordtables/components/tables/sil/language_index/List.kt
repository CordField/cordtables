package com.seedcompany.cordtables.components.tables.sil.language_index

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.language_index.languageIndex
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


data class SilLanguageIndexListRequest(
    val token: String?
)

data class SilLanguageIndexListResponse(
    val error: ErrorType,
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
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil-language-index/list")
    @ResponseBody
    fun listHandler(@RequestBody req:SilLanguageIndexListRequest): SilLanguageIndexListResponse {
        var data: MutableList<languageIndex> = mutableListOf()
        if (req.token == null) return SilLanguageIndexListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.language_index",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "common_id",
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
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var common_id: Int? = jdbcResult.getInt("common_id")
                if (jdbcResult.wasNull()) common_id = null

                var lang: String? = jdbcResult.getString("lang")
                if (jdbcResult.wasNull()) lang = null

                var country: String? = jdbcResult.getString("country")
                if (jdbcResult.wasNull()) country = null

                var name_type: String? = jdbcResult.getString("name_type")
                if (jdbcResult.wasNull()) name_type = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null



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
                    languageIndex(
                        id = id,
                        common_id = common_id,
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
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilLanguageIndexListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return SilLanguageIndexListResponse(ErrorType.NoError, data)
    }
}

