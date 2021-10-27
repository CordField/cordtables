package com.seedcompany.cordtables.components.tables.sc.languages

import com.seedcompany.cordtables.common.CommonSensitivity
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

data class ScLanguagesCreateRequest(
    val token: String? = null,
    val language: LanguageInput,
)

data class ScLanguagesCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
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

    @PostMapping("sc-languages/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScLanguagesCreateRequest): ScLanguagesCreateResponse {

        if (req.token == null) return ScLanguagesCreateResponse(error = ErrorType.InputMissingToken, null)
        if (req.language == null) return ScLanguagesCreateResponse(error = ErrorType.MissingId, null)
        if (req.language.name == null) return ScLanguagesCreateResponse(error = ErrorType.InputMissingName, null)
        if (req.language.display_name == null) return ScLanguagesCreateResponse(error = ErrorType.InputMissingName, null)

        // check enums and error out if needed

        if (req.language.sensitivity != null && !enumContains<CommonSensitivity>(req.language.sensitivity)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.egids_level != null && !enumContains<EgidsScale>(req.language.egids_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.least_reached_progress_jps_level != null && !enumContains<EgidsScale>(req.language.least_reached_progress_jps_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.partner_interest_level != null && !enumContains<EgidsScale>(req.language.partner_interest_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.multiple_languages_leverage_linguistic_level != null && !enumContains<EgidsScale>(req.language.multiple_languages_leverage_linguistic_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.multiple_languages_leverage_joint_training_level != null && !enumContains<EgidsScale>(req.language.multiple_languages_leverage_joint_training_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.lang_comm_int_in_language_development_level != null && !enumContains<EgidsScale>(req.language.lang_comm_int_in_language_development_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.lang_comm_int_in_scripture_translation_level != null && !enumContains<EgidsScale>(req.language.lang_comm_int_in_scripture_translation_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.access_to_scripture_in_lwc_level != null && !enumContains<EgidsScale>(req.language.access_to_scripture_in_lwc_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.begin_work_geo_challenges_level != null && !enumContains<EgidsScale>(req.language.begin_work_geo_challenges_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.begin_work_rel_pol_obstacles_level != null && !enumContains<EgidsScale>(req.language.begin_work_rel_pol_obstacles_level)) {
            return ScLanguagesCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.languages(name, display_name, created_by, modified_by, owning_person, owning_group)
                values(
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
            req.language.name,
            req.language.display_name,
            req.token,
            req.token,
            req.token,
        )

        req.language.id = id

        val updateResponse = update.updateHandler(
            ScLanguagesUpdateRequest(
                token = req.token,
                language = req.language,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScLanguagesCreateResponse(updateResponse.error)
        }

        return ScLanguagesCreateResponse(error = ErrorType.NoError, id = id)
    }


}