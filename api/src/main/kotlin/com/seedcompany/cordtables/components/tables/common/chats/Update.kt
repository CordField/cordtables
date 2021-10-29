package com.seedcompany.cordtables.components.tables.common.chats

import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.chats.CommonChatsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.chats.CommonChatsUpdateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonChatsUpdateRequest(
    val token: String?,
    val chat: ChatInput? = null,
)

data class CommonChatsUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonChatsUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-chats/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonChatsUpdateRequest): CommonChatsUpdateResponse {

        if (req.token == null) return CommonChatsUpdateResponse(ErrorType.TokenNotFound)
        if (req.chat == null) return CommonChatsUpdateResponse(ErrorType.MissingId)
        if (req.chat.id == null) return CommonChatsUpdateResponse(ErrorType.MissingId)

        if (req.chat.sensitivity != null && !enumContains<CommonSensitivity>(req.chat.sensitivity)) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.egids_level != null && !enumContains<EgidsScale>(req.chat.egids_level)) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.least_reached_progress_jps_level != null && !enumContains<LeastReachedProgressScale>(req.chat.least_reached_progress_jps_level)) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.partner_interest_level != null && !enumContains<PartnerInterestScale>(req.chat.partner_interest_level)) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.multiple_languages_leverage_linguistic_level != null && !enumContains<MultipleLanguagesLeverageLinguisticScale>(
                        req.chat.multiple_languages_leverage_linguistic_level
                )
        ) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.multiple_languages_leverage_joint_training_level != null && !enumContains<MultipleLanguagesLeverageJointTrainingScale>(
                        req.chat.multiple_languages_leverage_joint_training_level
                )
        ) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.lang_comm_int_in_language_development_level != null && !enumContains<LangCommIntInLanguageDevelopmentScale>(
                        req.chat.lang_comm_int_in_language_development_level
                )
        ) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.lang_comm_int_in_scripture_translation_level != null && !enumContains<LangCommIntInScriptureTranslationScale>(
                        req.chat.lang_comm_int_in_scripture_translation_level
                )
        ) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.access_to_scripture_in_lwc_level != null && !enumContains<AccessToScriptureInLwcScale>(req.chat.access_to_scripture_in_lwc_level)) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.begin_work_geo_challenges_level != null && !enumContains<BeginWorkGeoChallengesScale>(req.chat.begin_work_geo_challenges_level)) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.begin_work_rel_pol_obstacles_level != null && !enumContains<BeginWorkRelPolObstaclesScale>(
                        req.chat.begin_work_rel_pol_obstacles_level
                )
        ) {
            return CommonChatsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.neo4j_id != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "neo4j_id",
                id = req.chat.id!!,
                value = req.chat.neo4j_id,
        )

        if (req.chat.ethnologue != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "ethnologue",
                id = req.chat.id!!,
                value = req.chat.ethnologue,
        )

        if (req.chat.name != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "name",
                id = req.chat.id!!,
                value = req.chat.name,
        )

        if (req.chat.display_name != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "display_name",
                id = req.chat.id!!,
                value = req.chat.display_name,
        )

        if (req.chat.display_name_pronunciation != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "display_name_pronunciation",
                id = req.chat.id!!,
                value = req.chat.display_name_pronunciation,
        )

        if (req.chat.tags != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "tags",
                id = req.chat.id!!,
                value = req.chat.tags,
        )

        if (req.chat.preset_inventory is Boolean?) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "preset_inventory",
                id = req.chat.id!!,
                value = req.chat.preset_inventory,
        )

        if (req.chat.is_dialect is Boolean?) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "is_dialect",
                id = req.chat.id!!,
                value = req.chat.is_dialect,
        )

        if (req.chat.is_sign_language is Boolean?) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "is_sign_language",
                id = req.chat.id!!,
                value = req.chat.is_sign_language,
        )

        if (req.chat.is_least_of_these is Boolean?) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "is_least_of_these",
                id = req.chat.id!!,
                value = req.chat.is_least_of_these,
        )

        if (req.chat.least_of_these_reason != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "least_of_these_reason",
                id = req.chat.id!!,
                value = req.chat.least_of_these_reason,
        )

        if (req.chat.population_override != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "population_override",
                id = req.chat.id!!,
                value = req.chat.population_override,
        )

        if (req.chat.registry_of_dialects_code != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "registry_of_dialects_code",
                id = req.chat.id!!,
                value = req.chat.registry_of_dialects_code,
        )

        if (req.chat.sensitivity != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "sensitivity",
                id = req.chat.id!!,
                value = req.chat.sensitivity,
                cast = "::common.sensitivity"
        )

        if (req.chat.sign_language_code != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "sign_language_code",
                id = req.chat.id!!,
                value = req.chat.sign_language_code,
        )

        if (req.chat.sponsor_estimated_end_date != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "sponsor_estimated_end_date",
                id = req.chat.id!!,
                value = req.chat.sponsor_estimated_end_date,
        )

        if (req.chat.progress_bible is Boolean?) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "progress_bible",
                id = req.chat.id!!,
                value = req.chat.progress_bible,
        )

        if (req.chat.location_long != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "location_long",
                id = req.chat.id!!,
                value = req.chat.location_long,
        )

        if (req.chat.island != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "island",
                id = req.chat.id!!,
                value = req.chat.island,
        )

        if (req.chat.province != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "province",
                id = req.chat.id!!,
                value = req.chat.province,
        )

        if (req.chat.first_language_population != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "first_language_population",
                id = req.chat.id!!,
                value = req.chat.first_language_population,
        )

        if (req.chat.first_language_population != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "population_value",
                id = req.chat.id!!,
                value = getPopulationValue(req.chat.first_language_population),
        )

        if (req.chat.egids_level is String || req.chat.egids_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "egids_level",
                id = req.chat.id!!,
                value = req.chat.egids_level,
                cast = "::sc.egids_scale",
        )

        if (req.chat.egids_level is String || req.chat.egids_level.isNullOrBlank())
            util.updateField(
                    token = req.token,
                    table = "common.chats",
                    column = "egids_value",
                    id = req.chat.id!!,
                    value = if (req.chat.egids_level.isNullOrBlank()) 0.0 else EgidsScale.valueOf(req.chat.egids_level!!).value,
            )


        if (req.chat.least_reached_progress_jps_level is String || req.chat.least_reached_progress_jps_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "least_reached_progress_jps_level",
                id = req.chat.id!!,
                value = req.chat.least_reached_progress_jps_level,
                cast = "::sc.least_reached_progress_scale"
        )

        if (req.chat.least_reached_progress_jps_level is String || req.chat.least_reached_progress_jps_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "least_reached_value",
                id = req.chat.id!!,
                value = if (req.chat.least_reached_progress_jps_level.isNullOrBlank()) 0.0 else LeastReachedProgressScale.valueOf(req.chat.least_reached_progress_jps_level!!).value,
        )

        if (req.chat.partner_interest_level is String || req.chat.partner_interest_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "partner_interest_level",
                id = req.chat.id!!,
                value = req.chat.partner_interest_level,
                cast = "::sc.partner_interest_scale",
        )

        if (req.chat.partner_interest_level is String || req.chat.partner_interest_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "partner_interest_value",
                id = req.chat.id!!,
                value = if(req.chat.partner_interest_level.isNullOrBlank()) 0.0 else PartnerInterestScale.valueOf(req.chat.partner_interest_level!!).value,
        )

        if (req.chat.partner_interest_description != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "partner_interest_description",
                id = req.chat.id!!,
                value = req.chat.partner_interest_description,
        )

        if (req.chat.partner_interest_source != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "partner_interest_source",
                id = req.chat.id!!,
                value = req.chat.partner_interest_source,
        )

        if (req.chat.multiple_languages_leverage_linguistic_level is String || req.chat.multiple_languages_leverage_linguistic_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "multiple_languages_leverage_linguistic_level",
                id = req.chat.id!!,
                value = req.chat.multiple_languages_leverage_linguistic_level,
                cast = "::sc.multiple_languages_leverage_linguistic_scale",
        )

        if (req.chat.multiple_languages_leverage_linguistic_level is String || req.chat.multiple_languages_leverage_linguistic_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "multiple_languages_leverage_linguistic_value",
                id = req.chat.id!!,
                value = if(req.chat.multiple_languages_leverage_linguistic_level.isNullOrBlank()) 0.0 else MultipleLanguagesLeverageLinguisticScale.valueOf(req.chat.multiple_languages_leverage_linguistic_level!!).value,
        )

        if (req.chat.multiple_languages_leverage_linguistic_description != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "multiple_languages_leverage_linguistic_description",
                id = req.chat.id!!,
                value = req.chat.multiple_languages_leverage_linguistic_description,
        )

        if (req.chat.multiple_languages_leverage_linguistic_source != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "multiple_languages_leverage_linguistic_source",
                id = req.chat.id!!,
                value = req.chat.multiple_languages_leverage_linguistic_source,
        )

        if (req.chat.multiple_languages_leverage_joint_training_level is String || req.chat.multiple_languages_leverage_joint_training_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "multiple_languages_leverage_joint_training_level",
                id = req.chat.id!!,
                value = req.chat.multiple_languages_leverage_joint_training_level,
                cast = "::sc.multiple_languages_leverage_joint_training_scale",
        )

        if (req.chat.multiple_languages_leverage_joint_training_level is String || req.chat.multiple_languages_leverage_joint_training_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "multiple_languages_leverage_joint_training_value",
                id = req.chat.id!!,
                value = if(req.chat.multiple_languages_leverage_joint_training_level.isNullOrBlank()) 0.0 else MultipleLanguagesLeverageJointTrainingScale.valueOf(req.chat.multiple_languages_leverage_joint_training_level!!).value,
        )

        if (req.chat.multiple_languages_leverage_joint_training_description != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "multiple_languages_leverage_joint_training_description",
                id = req.chat.id!!,
                value = req.chat.multiple_languages_leverage_joint_training_description,
        )

        if (req.chat.multiple_languages_leverage_joint_training_source != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "multiple_languages_leverage_joint_training_source",
                id = req.chat.id!!,
                value = req.chat.multiple_languages_leverage_joint_training_source,
        )

        if (req.chat.lang_comm_int_in_language_development_level is String || req.chat.lang_comm_int_in_language_development_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "lang_comm_int_in_language_development_level",
                id = req.chat.id!!,
                value = req.chat.lang_comm_int_in_language_development_level,
                cast = "::sc.lang_comm_int_in_language_development_scale",
        )

        if (req.chat.lang_comm_int_in_language_development_level is String || req.chat.lang_comm_int_in_language_development_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "lang_comm_int_in_language_development_value",
                id = req.chat.id!!,
                value = if(req.chat.lang_comm_int_in_language_development_level.isNullOrBlank()) 0.0 else LangCommIntInLanguageDevelopmentScale.valueOf(req.chat.lang_comm_int_in_language_development_level!!).value,
        )

        if (req.chat.lang_comm_int_in_language_development_description != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "lang_comm_int_in_language_development_description",
                id = req.chat.id!!,
                value = req.chat.lang_comm_int_in_language_development_description,
        )

        if (req.chat.lang_comm_int_in_language_development_source != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "lang_comm_int_in_language_development_source",
                id = req.chat.id!!,
                value = req.chat.lang_comm_int_in_language_development_source,
        )

        if (req.chat.lang_comm_int_in_scripture_translation_level is String || req.chat.lang_comm_int_in_scripture_translation_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "lang_comm_int_in_scripture_translation_level",
                id = req.chat.id!!,
                value = req.chat.lang_comm_int_in_scripture_translation_level,
                cast = "::sc.lang_comm_int_in_scripture_translation_scale",
        )

        if (req.chat.lang_comm_int_in_scripture_translation_level is String || req.chat.lang_comm_int_in_scripture_translation_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "lang_comm_int_in_scripture_translation_value",
                id = req.chat.id!!,
                value = if(req.chat.lang_comm_int_in_scripture_translation_level.isNullOrBlank()) 0.0 else LangCommIntInScriptureTranslationScale.valueOf(req.chat.lang_comm_int_in_scripture_translation_level!!).value,
        )

        if (req.chat.lang_comm_int_in_scripture_translation_description != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "lang_comm_int_in_scripture_translation_description",
                id = req.chat.id!!,
                value = req.chat.lang_comm_int_in_scripture_translation_description,
        )

        if (req.chat.lang_comm_int_in_scripture_translation_source != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "lang_comm_int_in_scripture_translation_source",
                id = req.chat.id!!,
                value = req.chat.lang_comm_int_in_scripture_translation_source,
        )

        if (req.chat.access_to_scripture_in_lwc_level is String || req.chat.access_to_scripture_in_lwc_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "access_to_scripture_in_lwc_level",
                id = req.chat.id!!,
                value = req.chat.access_to_scripture_in_lwc_level,
                cast = "::sc.access_to_scripture_in_lwc_scale",
        )

        if (req.chat.access_to_scripture_in_lwc_level is String || req.chat.access_to_scripture_in_lwc_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "access_to_scripture_in_lwc_value",
                id = req.chat.id!!,
                value = if(req.chat.access_to_scripture_in_lwc_level.isNullOrBlank()) 0.0 else AccessToScriptureInLwcScale.valueOf(req.chat.access_to_scripture_in_lwc_level!!).value,
        )

        if (req.chat.access_to_scripture_in_lwc_description != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "access_to_scripture_in_lwc_description",
                id = req.chat.id!!,
                value = req.chat.access_to_scripture_in_lwc_description,
        )

        if (req.chat.access_to_scripture_in_lwc_source != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "access_to_scripture_in_lwc_source",
                id = req.chat.id!!,
                value = req.chat.access_to_scripture_in_lwc_source,
        )

        if (req.chat.begin_work_geo_challenges_level is String || req.chat.begin_work_geo_challenges_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "begin_work_geo_challenges_level",
                id = req.chat.id!!,
                value = req.chat.begin_work_geo_challenges_level,
                cast = "::sc.begin_work_geo_challenges_scale",
        )

        if (req.chat.begin_work_geo_challenges_level is String || req.chat.begin_work_geo_challenges_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "begin_work_geo_challenges_value",
                id = req.chat.id!!,
                value = if(req.chat.begin_work_geo_challenges_level.isNullOrBlank()) 0.0 else BeginWorkGeoChallengesScale.valueOf(req.chat.begin_work_geo_challenges_level!!).value,
        )

        if (req.chat.begin_work_geo_challenges_description != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "begin_work_geo_challenges_description",
                id = req.chat.id!!,
                value = req.chat.begin_work_geo_challenges_description,
        )

        if (req.chat.begin_work_geo_challenges_source != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "begin_work_geo_challenges_source",
                id = req.chat.id!!,
                value = req.chat.begin_work_geo_challenges_source,
        )

        if (req.chat.begin_work_rel_pol_obstacles_level is String || req.chat.begin_work_rel_pol_obstacles_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "begin_work_rel_pol_obstacles_level",
                id = req.chat.id!!,
                value = req.chat.begin_work_rel_pol_obstacles_level,
                cast = "::sc.begin_work_rel_pol_obstacles_scale",
        )

        if (req.chat.begin_work_rel_pol_obstacles_level is String || req.chat.begin_work_rel_pol_obstacles_level.isNullOrBlank()) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "begin_work_rel_pol_obstacles_value",
                id = req.chat.id!!,
                value = if(req.chat.begin_work_rel_pol_obstacles_level.isNullOrBlank()) 0.0 else BeginWorkRelPolObstaclesScale.valueOf(req.chat.begin_work_rel_pol_obstacles_level!!).value,
        )

        if (req.chat.begin_work_rel_pol_obstacles_description != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "begin_work_rel_pol_obstacles_description",
                id = req.chat.id!!,
                value = req.chat.begin_work_rel_pol_obstacles_description,
        )

        if (req.chat.begin_work_rel_pol_obstacles_source != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "begin_work_rel_pol_obstacles_source",
                id = req.chat.id!!,
                value = req.chat.begin_work_rel_pol_obstacles_source,
        )

        if (req.chat.suggested_strategies != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "suggested_strategies",
                id = req.chat.id!!,
                value = req.chat.suggested_strategies,
        )

        if (req.chat.comments != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "comments",
                id = req.chat.id!!,
                value = req.chat.comments,
        )

        if (req.chat.owning_person != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "owning_person",
                id = req.chat.id!!,
                value = req.chat.owning_person,
        )

        if (req.chat.owning_group != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "owning_group",
                id = req.chat.id!!,
                value = req.chat.owning_group,
        )

        if (req.chat.peer != null) util.updateField(
                token = req.token,
                table = "common.chats",
                column = "peer",
                id = req.chat.id!!,
                value = req.chat.peer,
        )

        return CommonChatsUpdateResponse(ErrorType.NoError)
    }

    fun getPopulationValue(population: Int): Float {
        return when (population) {
            in 1..100 -> 0.000F
            in 101..500 -> 0.125F
            in 501..1000 -> 0.250F
            in 1001..10000 -> 0.375F
            in 10001..100000 -> 0.500F
            in 100001..1000000 -> 0.625F
            in 1000001..10000000 -> 0.750F
            in 10000000..100000000 -> 0.875F
            else -> 1.000F
        }
    }
}
