package com.seedcompany.cordtables.components.tables.common.site_text_translations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonSiteTextTranslationCreateRequest(
        val token: String? = null,
        val site_text_translation: CommonSiteTextTranslationInput,
)

data class CommonSiteTextTranslationCreateResponse(
        val error: ErrorType,
        val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonSiteTextTranslationCreate")
class Create(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val update: Update,

        @Autowired
        val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common-site-text-translations/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonSiteTextTranslationCreateRequest): CommonSiteTextTranslationCreateResponse {

        if (req.token == null) return CommonSiteTextTranslationCreateResponse(error = ErrorType.InputMissingToken, null)
        if (req.site_text_translation.site_text_id == null) return CommonSiteTextTranslationCreateResponse(error = ErrorType.InputMissingColumn, null)
        if(req.site_text_translation.text_id == null) return CommonSiteTextTranslationCreateResponse(error = ErrorType.InputMissingColumn, null)
        if(req.site_text_translation.text_translation == null) return CommonSiteTextTranslationCreateResponse(error = ErrorType.InputMissingColumn, null)

        try {
        val id = jdbcTemplate.queryForObject("""
            insert into common.site_text_translations(
                site_text_id,
                text_id,
                text_translation,
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
            values(
                ?,
                ?,
                ?,
                (
                  select person 
                  from admin.tokens 
                  where token = ?
                ),
                (
                  select person 
                  from admin.tokens 
                  where token = ?
                ),
                (
                  select person 
                  from admin.tokens 
                  where token = ?
                ),
                1
            )
            returning id;
            """.trimIndent(),
                Int::class.java,
                req.site_text_translation.site_text_id,
                req.site_text_translation.text_id,
                req.site_text_translation.text_translation,
                req.token,
                req.token,
                req.token,
            )

            return CommonSiteTextTranslationCreateResponse(error = ErrorType.NoError, id = id)

        } catch (e: SQLException) {
            println(e.message)
            return CommonSiteTextTranslationCreateResponse(ErrorType.SQLInsertError, null)
        }
    }
}