package com.seedcompany.cordtables.components.tables.sc.languages

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguagesCreateRequest(
    val token: String? = null,
    val language: LanguageInput,
)

data class ScLanguagesCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguagesCreate")
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

    @PostMapping("sc/languages/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScLanguagesCreateRequest): ScLanguagesCreateResponse {

        if (req.token == null) return ScLanguagesCreateResponse(error = ErrorType.InputMissingToken, null)


        // looks like we can get away with this...
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.languages(
                display_name, 
                display_name_pronunciation, 
                ethnologue, 
                has_external_first_scripture,
                is_dialect,                       
                is_sign_language, 
                is_least_of_these, 
                least_of_these_reason, 
                name,
                population_override,
                registry_of_dialects_code,
                sensitivity,
                sign_language_code,
                sponsor_estimated_end_date, 
                tags, 
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
                values(
                    ?,
                    ?,
                    ?::uuid,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    '${req.language.sensitivity}',
                    ?,
                    ?::timestamp,
                    '${req.language.tags?.joinToString(separator = ",", prefix = "{", postfix = "}") {"\"${it}\""}}',
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
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.language.display_name,
            req.language.display_name_pronunciation,
            req.language.ethnologue,
            req.language.has_external_first_scripture,
            req.language.is_dialect,
            req.language.is_sign_language,
            req.language.is_least_of_these,
            req.language.least_of_these_reason,
            req.language.name,
            req.language.population_override,
            req.language.registry_of_dialects_code,
            req.language.sign_language_code,
            req.language.sponsor_estimated_end_date,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

//        req.language.id = id

        return ScLanguagesCreateResponse(error = ErrorType.NoError, id = id)
    }


}
