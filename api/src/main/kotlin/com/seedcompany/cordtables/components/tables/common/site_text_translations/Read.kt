package com.seedcompany.cordtables.components.tables.common.site_text_translations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
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

data class CommonSiteTextTranslationReadRequest(
        val token: String?,
        val id: Int? = null,
)

data class CommonSiteTextTranslationReadResponse(
        val error: ErrorType,
        val site_text_translation: CommonSiteTextTranslation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonSiteTextTranslationRead")
class Read(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-site-text-translations/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonSiteTextTranslationReadRequest): CommonSiteTextTranslationReadResponse {

        if (req.token == null) return CommonSiteTextTranslationReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonSiteTextTranslationReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
                GetSecureListQueryRequest(
                        tableName = "common.site_text_translations",
                        getList = false,
                        columns = arrayOf(
                                "id",
                                "site_text_id",
                                "text_id",
                                "text_translation",
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

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var site_text_id: Int? = jdbcResult.getInt("site_text_id")
                if (jdbcResult.wasNull()) site_text_id = null

                var text_id: String? = jdbcResult.getString("text_id")
                if (jdbcResult.wasNull()) text_id = null

                var text_translation: String? = jdbcResult.getString("text_translation")
                if (jdbcResult.wasNull()) text_translation = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val site_text_translation = CommonSiteTextTranslation(
                    id = id,
                    site_text_id = site_text_id,
                    text_id = text_id,
                    text_translation = text_translation,
                    created_at = created_at,
                    created_by = created_by,
                    modified_at = modified_at,
                    modified_by = modified_by,
                    owning_person = owning_person,
                    owning_group = owning_group
                )

                return CommonSiteTextTranslationReadResponse(ErrorType.NoError, site_text_translation = site_text_translation)
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonSiteTextTranslationReadResponse(ErrorType.SQLReadError)
        }

        return CommonSiteTextTranslationReadResponse(error = ErrorType.UnknownError)
    }
}
