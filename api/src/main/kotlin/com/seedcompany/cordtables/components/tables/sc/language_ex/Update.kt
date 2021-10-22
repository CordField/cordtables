package com.seedcompany.cordtables.components.tables.sc.language_ex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.languageex.EgidsScale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class LanguageExUpdateRequest(
    val token: String?,
    val languageEx: LanguageExInput? = null,
)

data class LanguageExUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SCLanguagesExUpdate2")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-languages-ex/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: LanguageExUpdateRequest): LanguageExUpdateResponse {

        if (req.token == null) return LanguageExUpdateResponse(ErrorType.TokenNotFound)
        if (req.languageEx == null) return LanguageExUpdateResponse(ErrorType.MissingId)
        if (req.languageEx.id == null) return LanguageExUpdateResponse(ErrorType.MissingId)

        if (req.languageEx.egids_level != null && !enumContains<EgidsScale>(req.languageEx.egids_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.least_reached_progress_jps_level != null && !enumContains<EgidsScale>(req.languageEx.least_reached_progress_jps_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.partner_interest_level != null && !enumContains<EgidsScale>(req.languageEx.partner_interest_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.multiple_languages_leverage_linguistic_level != null && !enumContains<EgidsScale>(req.languageEx.multiple_languages_leverage_linguistic_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.multiple_languages_leverage_joint_training_level != null && !enumContains<EgidsScale>(req.languageEx.multiple_languages_leverage_joint_training_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.lang_comm_int_in_language_development_level != null && !enumContains<EgidsScale>(req.languageEx.lang_comm_int_in_language_development_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.lang_comm_int_in_scripture_translation_level != null && !enumContains<EgidsScale>(req.languageEx.lang_comm_int_in_scripture_translation_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.access_to_scripture_in_lwc_level != null && !enumContains<EgidsScale>(req.languageEx.access_to_scripture_in_lwc_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.begin_work_geo_challenges_level != null && !enumContains<EgidsScale>(req.languageEx.begin_work_geo_challenges_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.begin_work_rel_pol_obstacles_level != null && !enumContains<EgidsScale>(req.languageEx.begin_work_rel_pol_obstacles_level)) {
            return LanguageExUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.languageEx.language_name != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "language_name",
            id = req.languageEx.id!!,
            value = req.languageEx.language_name,
        )

        if (req.languageEx.iso != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "iso",
            id = req.languageEx.id!!,
            value = req.languageEx.iso,
        )

        if (req.languageEx.progress_bible != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "progress_bible",
            id = req.languageEx.id!!,
            value = req.languageEx.progress_bible,
        )

        if (req.languageEx.location_long != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "location_long",
            id = req.languageEx.id!!,
            value = req.languageEx.location_long,
        )

        if (req.languageEx.island != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "island",
            id = req.languageEx.id!!,
            value = req.languageEx.island,
        )

        if (req.languageEx.province != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "province",
            id = req.languageEx.id!!,
            value = req.languageEx.province,
        )

        if (req.languageEx.first_language_population != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "first_language_population",
            id = req.languageEx.id!!,
            value = req.languageEx.first_language_population,
        )

        if (req.languageEx.egids_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "egids_level",
            id = req.languageEx.id!!,
            value = req.languageEx.egids_level,
            cast = "::sc.egids_scale",
        )

        if (req.languageEx.least_reached_progress_jps_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "least_reached_progress_jps_level",
            id = req.languageEx.id!!,
            value = req.languageEx.least_reached_progress_jps_level,
            cast = "::sc.least_reached_progress_jps_scale"
        )

        if (req.languageEx.partner_interest_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "partner_interest_level",
            id = req.languageEx.id!!,
            value = req.languageEx.partner_interest_level,
            cast = "::partner_interest_scale",
        )

        if (req.languageEx.partner_interest_description != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "partner_interest_description",
            id = req.languageEx.id!!,
            value = req.languageEx.partner_interest_description,
        )

        if (req.languageEx.partner_interest_source != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "partner_interest_source",
            id = req.languageEx.id!!,
            value = req.languageEx.partner_interest_source,
        )

        if (req.languageEx.multiple_languages_leverage_linguistic_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "multiple_languages_leverage_linguistic_level",
            id = req.languageEx.id!!,
            value = req.languageEx.multiple_languages_leverage_linguistic_level,
            cast = "::multiple_languages_leverage_linguistic_scale",
        )

        if (req.languageEx.multiple_languages_leverage_linguistic_description != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "multiple_languages_leverage_linguistic_description",
            id = req.languageEx.id!!,
            value = req.languageEx.multiple_languages_leverage_linguistic_description,
        )

        if (req.languageEx.multiple_languages_leverage_linguistic_source != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "multiple_languages_leverage_linguistic_source",
            id = req.languageEx.id!!,
            value = req.languageEx.multiple_languages_leverage_linguistic_source,
        )

        if (req.languageEx.multiple_languages_leverage_joint_training_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "multiple_languages_leverage_joint_training_level",
            id = req.languageEx.id!!,
            value = req.languageEx.multiple_languages_leverage_joint_training_level,
            cast = "::multiple_languages_leverage_joint_training_scale",
        )

        if (req.languageEx.multiple_languages_leverage_joint_training_description != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "multiple_languages_leverage_joint_training_description",
            id = req.languageEx.id!!,
            value = req.languageEx.multiple_languages_leverage_joint_training_description,
        )

        if (req.languageEx.multiple_languages_leverage_joint_training_source != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "multiple_languages_leverage_joint_training_source",
            id = req.languageEx.id!!,
            value = req.languageEx.multiple_languages_leverage_joint_training_source,
        )

        if (req.languageEx.lang_comm_int_in_language_development_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "lang_comm_int_in_language_development_level",
            id = req.languageEx.id!!,
            value = req.languageEx.lang_comm_int_in_language_development_level,
            cast = "::lang_comm_int_in_language_development_scale",
        )

        if (req.languageEx.lang_comm_int_in_language_development_description != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "lang_comm_int_in_language_development_description",
            id = req.languageEx.id!!,
            value = req.languageEx.lang_comm_int_in_language_development_description,
        )

        if (req.languageEx.lang_comm_int_in_language_development_source != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "lang_comm_int_in_language_development_source",
            id = req.languageEx.id!!,
            value = req.languageEx.lang_comm_int_in_language_development_source,
        )

        if (req.languageEx.lang_comm_int_in_scripture_translation_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "lang_comm_int_in_scripture_translation_level",
            id = req.languageEx.id!!,
            value = req.languageEx.lang_comm_int_in_scripture_translation_level,
            cast = "::lang_comm_int_in_scripture_translation_scale",
        )

        if (req.languageEx.lang_comm_int_in_scripture_translation_description != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "lang_comm_int_in_scripture_translation_description",
            id = req.languageEx.id!!,
            value = req.languageEx.lang_comm_int_in_scripture_translation_description,
        )

        if (req.languageEx.lang_comm_int_in_scripture_translation_source != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "lang_comm_int_in_scripture_translation_source",
            id = req.languageEx.id!!,
            value = req.languageEx.lang_comm_int_in_scripture_translation_source,
        )

        if (req.languageEx.access_to_scripture_in_lwc_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "access_to_scripture_in_lwc_level",
            id = req.languageEx.id!!,
            value = req.languageEx.access_to_scripture_in_lwc_level,
            cast = "::access_to_scripture_in_lwc_scale",
        )

        if (req.languageEx.access_to_scripture_in_lwc_description != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "access_to_scripture_in_lwc_description",
            id = req.languageEx.id!!,
            value = req.languageEx.access_to_scripture_in_lwc_description,
        )

        if (req.languageEx.access_to_scripture_in_lwc_source != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "access_to_scripture_in_lwc_source",
            id = req.languageEx.id!!,
            value = req.languageEx.access_to_scripture_in_lwc_source,
        )

        if (req.languageEx.begin_work_geo_challenges_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "begin_work_geo_challenges_level",
            id = req.languageEx.id!!,
            value = req.languageEx.begin_work_geo_challenges_level,
            cast = "::begin_work_geo_challenges_scale",
        )

        if (req.languageEx.begin_work_geo_challenges_description != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "begin_work_geo_challenges_description",
            id = req.languageEx.id!!,
            value = req.languageEx.begin_work_geo_challenges_description,
        )

        if (req.languageEx.begin_work_geo_challenges_source != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "begin_work_geo_challenges_source",
            id = req.languageEx.id!!,
            value = req.languageEx.begin_work_geo_challenges_source,
        )

        if (req.languageEx.begin_work_rel_pol_obstacles_level != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "begin_work_rel_pol_obstacles_level",
            id = req.languageEx.id!!,
            value = req.languageEx.begin_work_rel_pol_obstacles_level,
            cast = "::begin_work_rel_pol_obstacles_scale",
        )

        if (req.languageEx.begin_work_rel_pol_obstacles_description != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "begin_work_rel_pol_obstacles_description",
            id = req.languageEx.id!!,
            value = req.languageEx.begin_work_rel_pol_obstacles_description,
        )

        if (req.languageEx.begin_work_rel_pol_obstacles_source != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "begin_work_rel_pol_obstacles_source",
            id = req.languageEx.id!!,
            value = req.languageEx.begin_work_rel_pol_obstacles_source,
        )

        if (req.languageEx.suggested_strategies != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "suggested_strategies",
            id = req.languageEx.id!!,
            value = req.languageEx.suggested_strategies,
        )

        if (req.languageEx.comments != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "comments",
            id = req.languageEx.id!!,
            value = req.languageEx.comments,
        )

        if (req.languageEx.chat != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "chat",
            id = req.languageEx.id!!,
            value = req.languageEx.chat,
        )

        if (req.languageEx.owning_person != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "owning_person",
            id = req.languageEx.id!!,
            value = req.languageEx.owning_person,
        )

        if (req.languageEx.owning_group != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "owning_group",
            id = req.languageEx.id!!,
            value = req.languageEx.owning_group,
        )

        if (req.languageEx.peer != null) util.updateField(
            token = req.token,
            table = "sc.languages_ex",
            column = "peer",
            id = req.languageEx.id!!,
            value = req.languageEx.peer,
        )

        return LanguageExUpdateResponse(ErrorType.NoError)
    }
}
