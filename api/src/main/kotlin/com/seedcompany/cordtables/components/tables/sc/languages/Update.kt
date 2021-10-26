package com.seedcompany.cordtables.components.tables.sc.languages

import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource
import kotlin.reflect.full.memberProperties

data class ScLanguagesUpdateRequest(
    val token: String?,
    val language: LanguageInput? = null,
)

data class ScLanguagesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScLanguagesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-languages/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScLanguagesUpdateRequest): ScLanguagesUpdateResponse {


        if (req.token == null) return ScLanguagesUpdateResponse(ErrorType.TokenNotFound)
        if (req.language == null) return ScLanguagesUpdateResponse(ErrorType.MissingId)
        if (req.language.id == null) return ScLanguagesUpdateResponse(ErrorType.MissingId)



        if (req.language.sensitivity != null && !enumContains<CommonSensitivity>(req.language.sensitivity)) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.egids_level != null && !enumContains<EgidsScale>(req.language.egids_level)) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.least_reached_progress_jps_level != null && !enumContains<LeastReachedProgressScale>(req.language.least_reached_progress_jps_level)) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.partner_interest_level != null && !enumContains<PartnerInterestScale>(req.language.partner_interest_level)) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.multiple_languages_leverage_linguistic_level != null && !enumContains<MultipleLanguagesLeverageLinguisticScale>(
                req.language.multiple_languages_leverage_linguistic_level
            )
        ) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.multiple_languages_leverage_joint_training_level != null && !enumContains<MultipleLanguagesLeverageJointTrainingScale>(
                req.language.multiple_languages_leverage_joint_training_level
            )
        ) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.lang_comm_int_in_language_development_level != null && !enumContains<LangCommIntInLanguageDevelopmentScale>(
                req.language.lang_comm_int_in_language_development_level
            )
        ) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.lang_comm_int_in_scripture_translation_level != null && !enumContains<LangCommIntInScriptureTranslationScale>(
                req.language.lang_comm_int_in_scripture_translation_level
            )
        ) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.access_to_scripture_in_lwc_level != null && !enumContains<AccessToScriptureInLwcScale>(req.language.access_to_scripture_in_lwc_level)) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.begin_work_geo_challenges_level != null && !enumContains<BeginWorkGeoChallengesScale>(req.language.begin_work_geo_challenges_level)) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.begin_work_rel_pol_obstacles_level != null && !enumContains<BeginWorkRelPolObstaclesScale>(
                req.language.begin_work_rel_pol_obstacles_level
            )
        ) {
            return ScLanguagesUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.language.neo4j_id != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "neo4j_id",
            id = req.language.id!!,
            value = req.language.neo4j_id,
        )

        if (req.language.ethnologue != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "ethnologue",
            id = req.language.id!!,
            value = req.language.ethnologue,
        )

        if (req.language.name != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "name",
            id = req.language.id!!,
            value = req.language.name,
        )

        if (req.language.display_name != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "display_name",
            id = req.language.id!!,
            value = req.language.display_name,
        )

        if (req.language.display_name_pronunciation != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "display_name_pronunciation",
            id = req.language.id!!,
            value = req.language.display_name_pronunciation,
        )

        if (req.language.tags != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "tags",
            id = req.language.id!!,
            value = req.language.tags,
        )

        if (req.language.preset_inventory != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "preset_inventory",
            id = req.language.id!!,
            value = req.language.preset_inventory,
        )

        if (req.language.is_dialect != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "is_dialect",
            id = req.language.id!!,
            value = req.language.is_dialect,
        )

        if (req.language.is_sign_language != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "is_sign_language",
            id = req.language.id!!,
            value = req.language.is_sign_language,
        )

        if (req.language.is_least_of_these != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "is_least_of_these",
            id = req.language.id!!,
            value = req.language.is_least_of_these,
        )

        if (req.language.least_of_these_reason != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "least_of_these_reason",
            id = req.language.id!!,
            value = req.language.least_of_these_reason,
        )

        if (req.language.population_override != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "population_override",
            id = req.language.id!!,
            value = req.language.population_override,
        )

        if (req.language.registry_of_dialects_code != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "registry_of_dialects_code",
            id = req.language.id!!,
            value = req.language.registry_of_dialects_code,
        )

        if (req.language.sensitivity != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "sensitivity",
            id = req.language.id!!,
            value = req.language.sensitivity,
            cast = "::common.sensitivity"
        )

        if (req.language.sign_language_code != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "sign_language_code",
            id = req.language.id!!,
            value = req.language.sign_language_code,
        )

        if (req.language.sponsor_estimated_end_date != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "sponsor_estimated_end_date",
            id = req.language.id!!,
            value = req.language.sponsor_estimated_end_date,
        )

        if (req.language.progress_bible != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "progress_bible",
            id = req.language.id!!,
            value = req.language.progress_bible,
        )

        if (req.language.location_long != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "location_long",
            id = req.language.id!!,
            value = req.language.location_long,
        )

        if (req.language.island != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "island",
            id = req.language.id!!,
            value = req.language.island,
        )

        if (req.language.province != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "province",
            id = req.language.id!!,
            value = req.language.province,
        )

        if (req.language.first_language_population != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "first_language_population",
            id = req.language.id!!,
            value = req.language.first_language_population,
        )

        if (req.language.first_language_population != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "population_value",
            id = req.language.id!!,
            value = getPopulationValue(req.language.first_language_population),
        )

        if (req.language.egids_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "egids_level",
            id = req.language.id!!,
            value = req.language.egids_level,
            cast = "::sc.egids_scale",
        )

        if (req.language.egids_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "egids_value",
            id = req.language.id!!,
            value = EgidsScale.valueOf(req.language.egids_level!!).value,
        )

        if (req.language.least_reached_progress_jps_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "least_reached_progress_jps_level",
            id = req.language.id!!,
            value = req.language.least_reached_progress_jps_level,
            cast = "::sc.least_reached_progress_scale"
        )

        if (req.language.least_reached_progress_jps_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "least_reached_value",
            id = req.language.id!!,
            value = LeastReachedProgressScale.valueOf(req.language.least_reached_progress_jps_level).value,
        )

        if (req.language.partner_interest_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "partner_interest_level",
            id = req.language.id!!,
            value = req.language.partner_interest_level,
            cast = "::sc.partner_interest_scale",
        )

        if (req.language.partner_interest_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "partner_interest_value",
            id = req.language.id!!,
            value = PartnerInterestScale.valueOf(req.language.partner_interest_level).value,
        )

        if (req.language.partner_interest_description != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "partner_interest_description",
            id = req.language.id!!,
            value = req.language.partner_interest_description,
        )

        if (req.language.partner_interest_source != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "partner_interest_source",
            id = req.language.id!!,
            value = req.language.partner_interest_source,
        )

        if (req.language.multiple_languages_leverage_linguistic_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "multiple_languages_leverage_linguistic_level",
            id = req.language.id!!,
            value = req.language.multiple_languages_leverage_linguistic_level,
            cast = "::sc.multiple_languages_leverage_linguistic_scale",
        )

        if (req.language.multiple_languages_leverage_linguistic_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "multiple_languages_leverage_linguistic_value",
            id = req.language.id!!,
            value = MultipleLanguagesLeverageLinguisticScale.valueOf(req.language.multiple_languages_leverage_linguistic_level).value,
        )

        if (req.language.multiple_languages_leverage_linguistic_description != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "multiple_languages_leverage_linguistic_description",
            id = req.language.id!!,
            value = req.language.multiple_languages_leverage_linguistic_description,
        )

        if (req.language.multiple_languages_leverage_linguistic_source != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "multiple_languages_leverage_linguistic_source",
            id = req.language.id!!,
            value = req.language.multiple_languages_leverage_linguistic_source,
        )

        if (req.language.multiple_languages_leverage_joint_training_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "multiple_languages_leverage_joint_training_level",
            id = req.language.id!!,
            value = req.language.multiple_languages_leverage_joint_training_level,
            cast = "::sc.multiple_languages_leverage_joint_training_scale",
        )

        if (req.language.multiple_languages_leverage_joint_training_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "multiple_languages_leverage_joint_training_value",
            id = req.language.id!!,
            value = MultipleLanguagesLeverageJointTrainingScale.valueOf(req.language.multiple_languages_leverage_joint_training_level).value,
        )

        if (req.language.multiple_languages_leverage_joint_training_description != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "multiple_languages_leverage_joint_training_description",
            id = req.language.id!!,
            value = req.language.multiple_languages_leverage_joint_training_description,
        )

        if (req.language.multiple_languages_leverage_joint_training_source != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "multiple_languages_leverage_joint_training_source",
            id = req.language.id!!,
            value = req.language.multiple_languages_leverage_joint_training_source,
        )

        if (req.language.lang_comm_int_in_language_development_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "lang_comm_int_in_language_development_level",
            id = req.language.id!!,
            value = req.language.lang_comm_int_in_language_development_level,
            cast = "::sc.lang_comm_int_in_language_development_scale",
        )

        if (req.language.lang_comm_int_in_language_development_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "lang_comm_int_in_language_development_value",
            id = req.language.id!!,
            value = LangCommIntInLanguageDevelopmentScale.valueOf(req.language.lang_comm_int_in_language_development_level).value,
        )

        if (req.language.lang_comm_int_in_language_development_description != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "lang_comm_int_in_language_development_description",
            id = req.language.id!!,
            value = req.language.lang_comm_int_in_language_development_description,
        )

        if (req.language.lang_comm_int_in_language_development_source != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "lang_comm_int_in_language_development_source",
            id = req.language.id!!,
            value = req.language.lang_comm_int_in_language_development_source,
        )

        if (req.language.lang_comm_int_in_scripture_translation_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "lang_comm_int_in_scripture_translation_level",
            id = req.language.id!!,
            value = req.language.lang_comm_int_in_scripture_translation_level,
            cast = "::sc.lang_comm_int_in_scripture_translation_scale",
        )

        if (req.language.lang_comm_int_in_scripture_translation_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "lang_comm_int_in_scripture_translation_value",
            id = req.language.id!!,
            value = LangCommIntInScriptureTranslationScale.valueOf(req.language.lang_comm_int_in_scripture_translation_level).value,
        )

        if (req.language.lang_comm_int_in_scripture_translation_description != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "lang_comm_int_in_scripture_translation_description",
            id = req.language.id!!,
            value = req.language.lang_comm_int_in_scripture_translation_description,
        )

        if (req.language.lang_comm_int_in_scripture_translation_source != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "lang_comm_int_in_scripture_translation_source",
            id = req.language.id!!,
            value = req.language.lang_comm_int_in_scripture_translation_source,
        )

        if (req.language.access_to_scripture_in_lwc_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "access_to_scripture_in_lwc_level",
            id = req.language.id!!,
            value = req.language.access_to_scripture_in_lwc_level,
            cast = "::sc.access_to_scripture_in_lwc_scale",
        )

        if (req.language.access_to_scripture_in_lwc_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "access_to_scripture_in_lwc_value",
            id = req.language.id!!,
            value = AccessToScriptureInLwcScale.valueOf(req.language.access_to_scripture_in_lwc_level).value,
        )

        if (req.language.access_to_scripture_in_lwc_description != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "access_to_scripture_in_lwc_description",
            id = req.language.id!!,
            value = req.language.access_to_scripture_in_lwc_description,
        )

        if (req.language.access_to_scripture_in_lwc_source != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "access_to_scripture_in_lwc_source",
            id = req.language.id!!,
            value = req.language.access_to_scripture_in_lwc_source,
        )

        if (req.language.begin_work_geo_challenges_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "begin_work_geo_challenges_level",
            id = req.language.id!!,
            value = req.language.begin_work_geo_challenges_level,
            cast = "::sc.begin_work_geo_challenges_scale",
        )

        if (req.language.begin_work_geo_challenges_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "begin_work_geo_challenges_value",
            id = req.language.id!!,
            value = BeginWorkGeoChallengesScale.valueOf(req.language.begin_work_geo_challenges_level).value,
        )

        if (req.language.begin_work_geo_challenges_description != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "begin_work_geo_challenges_description",
            id = req.language.id!!,
            value = req.language.begin_work_geo_challenges_description,
        )

        if (req.language.begin_work_geo_challenges_source != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "begin_work_geo_challenges_source",
            id = req.language.id!!,
            value = req.language.begin_work_geo_challenges_source,
        )

        if (req.language.begin_work_rel_pol_obstacles_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "begin_work_rel_pol_obstacles_level",
            id = req.language.id!!,
            value = req.language.begin_work_rel_pol_obstacles_level,
            cast = "::sc.begin_work_rel_pol_obstacles_scale",
        )

        if (req.language.begin_work_rel_pol_obstacles_level != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "begin_work_rel_pol_obstacles_value",
            id = req.language.id!!,
            value = BeginWorkRelPolObstaclesScale.valueOf(req.language.begin_work_rel_pol_obstacles_level).value,
        )

        if (req.language.begin_work_rel_pol_obstacles_description != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "begin_work_rel_pol_obstacles_description",
            id = req.language.id!!,
            value = req.language.begin_work_rel_pol_obstacles_description,
        )

        if (req.language.begin_work_rel_pol_obstacles_source != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "begin_work_rel_pol_obstacles_source",
            id = req.language.id!!,
            value = req.language.begin_work_rel_pol_obstacles_source,
        )

        if (req.language.suggested_strategies != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "suggested_strategies",
            id = req.language.id!!,
            value = req.language.suggested_strategies,
        )

        if (req.language.comments != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "comments",
            id = req.language.id!!,
            value = req.language.comments,
        )

        if (req.language.owning_person != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "owning_person",
            id = req.language.id!!,
            value = req.language.owning_person,
        )

        if (req.language.owning_group != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "owning_group",
            id = req.language.id!!,
            value = req.language.owning_group,
        )

        if (req.language.peer != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "peer",
            id = req.language.id!!,
            value = req.language.peer,
        )

        return ScLanguagesUpdateResponse(ErrorType.NoError)
    }

    fun getPopulationValue(population: Int): Float{
        return when (population){
            in 1..100 -> 0.000F
            in 101..500 -> 0.125F
            in 501..1000 -> 0.250F
            in 1001..10000 -> 0.375F
            in 10001..100000 -> 0.500F
            in 100001..1000000 -> 0.625F
            in 1000001..10000000 -> 0.750F
            in 10000000.. 100000000 -> 0.875F
            else -> 1.000F
        }
    }
}
