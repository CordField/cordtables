package com.seedcompany.cordtables.components.tables.sc.language_ex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.languageex.EgidsScale
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
    val languageEx: LanguageExInput,
)

data class CreateLanguageExResponse(
    val error: ErrorType,
    val languageEx: LanguageEx? = null,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SCLanguageExCreate")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update2,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("language_ex/create")
    @ResponseBody
    fun CreateHandler(@RequestBody req: CreateLanguageExRequest): CreateLanguageExResponse {

        if (req.token == null) return CreateLanguageExResponse(error = ErrorType.InputMissingToken, null)

        // check enums and error out if needed

        if (req.languageEx.egids_level != null && !enumContains<EgidsScale>(req.languageEx.egids_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.least_reached_progress_jps_level != null && !enumContains<EgidsScale>(req.languageEx.least_reached_progress_jps_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.partner_interest_level != null && !enumContains<EgidsScale>(req.languageEx.partner_interest_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.multiple_languages_leverage_linguistic_level != null && !enumContains<EgidsScale>(req.languageEx.multiple_languages_leverage_linguistic_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.multiple_languages_leverage_joint_training_level != null && !enumContains<EgidsScale>(req.languageEx.multiple_languages_leverage_joint_training_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.lang_comm_int_in_language_development_level != null && !enumContains<EgidsScale>(req.languageEx.lang_comm_int_in_language_development_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.lang_comm_int_in_scripture_translation_level != null && !enumContains<EgidsScale>(req.languageEx.lang_comm_int_in_scripture_translation_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.access_to_scripture_in_lwc_level != null && !enumContains<EgidsScale>(req.languageEx.access_to_scripture_in_lwc_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.begin_work_geo_challenges_level != null && !enumContains<EgidsScale>(req.languageEx.begin_work_geo_challenges_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.begin_work_rel_pol_obstacles_level != null && !enumContains<EgidsScale>(req.languageEx.begin_work_rel_pol_obstacles_level)) {
            return CreateLanguageExResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.languages_ex(created_by, modified_by, owning_person, owning_group)
                values(
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
            req.token,
            req.token,
            req.token,
        )

        val response = update.updateHandler(
            UpdateLanguageExRequest2(
                token = req.token,
                languageEx = req.languageEx,
            )
        )

        if (response.error == ErrorType.NoError) {
            return CreateLanguageExResponse(error = ErrorType.NoError)
        }

        return CreateLanguageExResponse(error = ErrorType.UnknownError, null)
    }


}