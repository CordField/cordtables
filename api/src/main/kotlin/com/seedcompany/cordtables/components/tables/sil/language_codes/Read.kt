package com.seedcompany.cordtables.components.tables.sil.language_codes

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sil.language_codes.languageCode
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

data class SilLanguageCodesReadRequest(
    val token: String?,
    val id: String? = null,
)

data class SilLanguageCodesReadResponse(
    val error: ErrorType,
    val languageCode: languageCode? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilLanguageCodesRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sil-language-codes/read")
    @ResponseBody
    fun readHandler(@RequestBody req: SilLanguageCodesReadRequest): SilLanguageCodesReadResponse {

        if (req.token == null) return SilLanguageCodesReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return SilLanguageCodesReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sil.language_codes",
                getList = false,
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
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var lang: String? = jdbcResult.getString("lang")
                if (jdbcResult.wasNull()) lang = null

                var country: String? = jdbcResult.getString("country")
                if (jdbcResult.wasNull()) country = null

                var lang_status: String? = jdbcResult.getString("lang_status")
                if (jdbcResult.wasNull()) lang_status = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

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

                val languageCode =
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

                return SilLanguageCodesReadResponse(ErrorType.NoError, languageCode = languageCode)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return SilLanguageCodesReadResponse(ErrorType.SQLReadError)
        }

        return SilLanguageCodesReadResponse(error = ErrorType.UnknownError)
    }
}
