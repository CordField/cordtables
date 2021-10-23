package com.seedcompany.cordtables.components.tables.sc.language_ex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CreateLanguageExRequest(
    val token: String? = null,
    val language: LanguageExInput,
)

data class CreateLanguageExResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SCLanguageExCreate")
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

    @PostMapping("sc-languages/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CreateLanguageExRequest): CreateLanguageExResponse {

        if (req.token == null) return CreateLanguageExResponse(error = ErrorType.InputMissingToken, null)
        if (req.language == null) return CreateLanguageExResponse(error = ErrorType.MissingId, null)
        if (req.language.language_name == null) return CreateLanguageExResponse(error = ErrorType.InputMissingName, null)
        if (req.language.language_name.length > 32) return CreateLanguageExResponse(error = ErrorType.NameTooLong, null)

        // check enums and error out if needed

        if (req.language.egids_level != null && !enumContains<EgidsScale>(req.language.egids_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.least_reached_progress_jps_level != null && !enumContains<EgidsScale>(req.language.least_reached_progress_jps_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.partner_interest_level != null && !enumContains<EgidsScale>(req.language.partner_interest_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.multiple_languages_leverage_linguistic_level != null && !enumContains<EgidsScale>(req.language.multiple_languages_leverage_linguistic_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.multiple_languages_leverage_joint_training_level != null && !enumContains<EgidsScale>(req.language.multiple_languages_leverage_joint_training_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.lang_comm_int_in_language_development_level != null && !enumContains<EgidsScale>(req.language.lang_comm_int_in_language_development_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.lang_comm_int_in_scripture_translation_level != null && !enumContains<EgidsScale>(req.language.lang_comm_int_in_scripture_translation_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.access_to_scripture_in_lwc_level != null && !enumContains<EgidsScale>(req.language.access_to_scripture_in_lwc_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.begin_work_geo_challenges_level != null && !enumContains<EgidsScale>(req.language.begin_work_geo_challenges_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.begin_work_rel_pol_obstacles_level != null && !enumContains<EgidsScale>(req.language.begin_work_rel_pol_obstacles_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.languages_ex(language_name, created_by, modified_by, owning_person, owning_group)
                values(
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
            req.language.language_name,
            req.token,
            req.token,
            req.token,
        )

        req.language.id = id

        val updateResponse = update.updateHandler(
            LanguageExUpdateRequest(
                token = req.token,
                languageEx = req.language,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CreateLanguageExResponse(updateResponse.error)
        }

        return CreateLanguageExResponse(error = ErrorType.NoError, id = id)
    }


}