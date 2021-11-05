package com.seedcompany.cordtables.components.tables.sc.languages

import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.budget_records.ScLanguagesUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.budget_records.ScLanguagesUpdateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguagesUpdateRequest(
        val token: String?,
        val id: Int? = null,
        val column: String? = null,
        val value: Any? = null,
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
        if (req.column == null) return ScLanguagesUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScLanguagesUpdateResponse(ErrorType.MissingId)

        if (req.column.equals("sensitivity") && req.value != null && !enumContains<CommonSensitivity>(req.value as String)) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("egids_level") && req.value != null && !enumContains<EgidsScale>(req.value as String)) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("least_reached_progress_jps_level") && req.value != null && !enumContains<LeastReachedProgressScale>(req.value as String)) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("partner_interest_level") && req.value != null && !enumContains<PartnerInterestScale>(req.value as String)) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("multiple_languages_leverage_linguistic_level") && req.value != null && !enumContains<MultipleLanguagesLeverageLinguisticScale>(
                        req.value as String
                )
        ) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("multiple_languages_leverage_joint_training_level") && req.value != null && !enumContains<MultipleLanguagesLeverageJointTrainingScale>(
                        req.value as String
                )
        ) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("lang_comm_int_in_language_development_level") && req.value != null && !enumContains<LangCommIntInLanguageDevelopmentScale>(
                        req.value as String
                )
        ) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("lang_comm_int_in_scripture_translation_level") && req.value != null && !enumContains<LangCommIntInScriptureTranslationScale>(
                        req.value as String
                )
        ) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("access_to_scripture_in_lwc_level") && req.value != null && !enumContains<AccessToScriptureInLwcScale>(req.value as String)) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("begin_work_geo_challenges_level") && req.value != null && !enumContains<BeginWorkGeoChallengesScale>(req.value as String)) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.column.equals("begin_work_rel_pol_obstacles_level") && req.value != null && !enumContains<BeginWorkRelPolObstaclesScale>(
                        req.value as String
                )
        ) {
            return ScLanguagesUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        when (req.column) {
            "neo4j_id" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "neo4j_id",
                        id = req.id,
                        value = req.value,
                )
            }

            "ethnologue" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "ethnologue",
                        id = req.id,
                        value = req.value,
                )
            }


            "name" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "name",
                        id = req.id,
                        value = req.value,
                )
            }

            "display_name" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "display_name",
                        id = req.id,
                        value = req.value,
                )
            }

            "display_name_pronunciation" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "display_name_pronunciation",
                        id = req.id,
                        value = req.value,
                )
            }

            "tags" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "tags",
                        id = req.id,
                        value = req.value,
                )
            }

            "preset_inventory" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "preset_inventory",
                        id = req.id,
                        value = req.value,
                        cast = "::boolean"
                )
            }

            "is_dialect" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "is_dialect",
                        id = req.id,
                        value = req.value,
                        cast = "::boolean"
                )
            }

            "is_sign_language" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "is_sign_language",
                        id = req.id,
                        value = req.value,
                        cast = "::boolean"
                )
            }

            "is_least_of_these" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "is_least_of_these",
                        id = req.id,
                        value = req.value,
                        cast = "::boolean"
                )
            }
            "least_of_these_reason" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "least_of_these_reason",
                        id = req.id,
                        value = req.value,
                )
            }

            "population_override" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "population_override",
                        id = req.id,
                        value = req.value,
                )
            }

            "registry_of_dialects_code" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "registry_of_dialects_code",
                        id = req.id,
                        value = req.value,
                )
            }

            "sensitivity" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "sensitivity",
                        id = req.id,
                        value = req.value,
                        cast = "::common.sensitivity"
                )
            }

            "sign_language_code" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "sign_language_code",
                        id = req.id,
                        value = req.value,
                )
            }

            "sponsor_estimated_end_date" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "sponsor_estimated_end_date",
                        id = req.id,
                        value = req.value,
                )
            }

            "progress_bible" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "progress_bible",
                        id = req.id,
                        value = req.value,
                        cast = "::boolean"
                )
            }

            "location_long" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "location_long",
                        id = req.id,
                        value = req.value,
                )
            }

            "island" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "island",
                        id = req.id,
                        value = req.value,
                )
            }

            "province" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "province",
                        id = req.id,
                        value = req.value,
                )
            }

            "first_language_population" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "first_language_population",
                        id = req.id,
                        value = req.value,
                )
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "population_value",
                        id = req.id,
                        value = getPopulationValue(req.value as Int),
                )
            }


            "least_reached_progress_jps_level" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "least_reached_progress_jps_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.least_reached_progress_scale",
                )


                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "least_reached_value",
                        id = req.id,
                        value = if (req.value !== null) LeastReachedProgressScale.valueOf(req.value as String).value else 0.0,
                )
            }

            "partner_interest_level" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "partner_interest_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.partner_interest_scale",
                )


                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "partner_interest_value",
                        id = req.id,
                        value = if (req.value !== null) PartnerInterestScale.valueOf(req.value as String).value else 0.0,
                )
            }


            "partner_interest_description" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "partner_interest_description",
                        id = req.id,
                        value = req.value,
                )
            }

            "partner_interest_source" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "partner_interest_source",
                        id = req.id,
                        value = req.value,
                )
            }


            "multiple_languages_leverage_linguistic_level" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "multiple_languages_leverage_linguistic_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.multiple_languages_leverage_linguistic_scale",
                )


                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "multiple_languages_leverage_linguistic_value",
                        id = req.id,
                        value = if (req.value !== null) MultipleLanguagesLeverageLinguisticScale.valueOf(req.value as String).value else 0.0,
                )
            }


            "multiple_languages_leverage_linguistic_description"
            -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "multiple_languages_leverage_linguistic_description",
                        id = req.id,
                        value = req.value,
                )
            }

            "multiple_languages_leverage_linguistic_source" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "multiple_languages_leverage_linguistic_source",
                        id = req.id,
                        value = req.value,
                )
            }

            "multiple_languages_leverage_joint_training_level" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "multiple_languages_leverage_joint_training_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.multiple_languages_leverage_joint_training_level",
                )


                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "multiple_languages_leverage_joint_training_value",
                        id = req.id,
                        value = if (req.value !== null) MultipleLanguagesLeverageJointTrainingScale.valueOf(req.value as String).value else 0.0,
                )
            }


            "multiple_languages_leverage_joint_training_description"
            -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "multiple_languages_leverage_joint_training_description",
                        id = req.id,
                        value = req.value,
                )
            }

            "multiple_languages_leverage_joint_training_source" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "multiple_languages_leverage_joint_training_source",
                        id = req.id,
                        value = req.value,
                )
            }

            "lang_comm_int_in_language_development_level" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "lang_comm_int_in_language_development_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.lang_comm_int_in_language_development_scale",
                )


                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "lang_comm_int_in_language_development_value",
                        id = req.id,
                        value = if (req.value !== null) LangCommIntInLanguageDevelopmentScale.valueOf(req.value as String).value else 0.0,
                )
            }


            "lang_comm_int_in_language_development_description"
            -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "lang_comm_int_in_language_development_description",
                        id = req.id,
                        value = req.value,
                )
            }

            "lang_comm_int_in_language_development_source" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "lang_comm_int_in_language_development_source",
                        id = req.id,
                        value = req.value,
                )
            }

            "lang_comm_int_in_scripture_translation_level" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "lang_comm_int_in_scripture_translation_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.lang_comm_int_in_scripture_translation_scale",
                )


                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "lang_comm_int_in_scripture_translation_value",
                        id = req.id,
                        value = if (req.value !== null) LangCommIntInScriptureTranslationScale.valueOf(req.value as String).value else 0.0,
                )
            }


            "lang_comm_int_in_scripture_translation_description"
            -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "lang_comm_int_in_scripture_translation_description",
                        id = req.id,
                        value = req.value,
                )
            }

            "lang_comm_int_in_scripture_translation_source" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "lang_comm_int_in_scripture_translation_source",
                        id = req.id,
                        value = req.value,
                )
            }

            "access_to_scripture_in_lwc_level" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "access_to_scripture_in_lwc_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.access_to_scripture_in_lwc_scale",
                )


                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "access_to_scripture_in_lwc_value",
                        id = req.id,
                        value = if (req.value !== null) AccessToScriptureInLwcScale.valueOf(req.value as String).value else 0.0,
                )
            }


            "access_to_scripture_in_lwc_description"
            -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "access_to_scripture_in_lwc_description",
                        id = req.id,
                        value = req.value,
                )
            }

            "access_to_scripture_in_lwc_source" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "access_to_scripture_in_lwc_source",
                        id = req.id,
                        value = req.value,
                )
            }


            "begin_work_geo_challenges_level" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "begin_work_geo_challenges_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.begin_work_geo_challenges_scale",
                )


                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "begin_work_geo_challenges_value",
                        id = req.id,
                        value = if (req.value !== null) BeginWorkGeoChallengesScale.valueOf(req.value as String).value else 0.0,
                )
            }

            "begin_work_geo_challenges_description"
            -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "begin_work_geo_challenges_description",
                        id = req.id,
                        value = req.value,
                )
            }

            "begin_work_geo_challenges_source" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "begin_work_geo_challenges_source",
                        id = req.id,
                        value = req.value,
                )
            }


            "begin_work_rel_pol_obstacles_level" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "begin_work_rel_pol_obstacles_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.begin_work_rel_pol_obstacles_scale",
                )


                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "begin_work_rel_pol_obstacles_value",
                        id = req.id,
                        value = if (req.value !== null) BeginWorkRelPolObstaclesScale.valueOf(req.value as String).value else 0.0,
                )
            }


            "begin_work_rel_pol_obstacles_description"
            -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "begin_work_rel_pol_obstacles_description",
                        id = req.id,
                        value = req.value,
                )
            }

            "begin_work_rel_pol_obstacles_source" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "begin_work_rel_pol_obstacles_source",
                        id = req.id,
                        value = req.value,
                )
            }

            "suggested_strategies" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "suggested_strategies",
                        id = req.id,
                        value = req.value,
                )
            }

            "comments" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "comments",
                        id = req.id,
                        value = req.value,
                )
            }

            "owning_person" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "owning_person",
                        id = req.id,
                        value = req.value,
                )
            }

            "owning_group" -> {
                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "owning_group",
                        id = req.id,
                        value = req.value,
                )
            }

            "egids_level" -> {

                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "egids_level",
                        id = req.id,
                        value = req.value,
                        cast = "::sc.egids_scale",
                )

                util.updateField(
                        token = req.token,
                        table = "sc.languages",
                        column = "egids_value",
                        id = req.id,
                        value = if (req.value !== null) EgidsScale.valueOf(req.value as String).value else 0.0,
                )
            }

            "coordinates" -> {
                    var coordinatesValue:String?;
                    var longLatValue: String = ""
                    if(req.value == null) {
                        coordinatesValue = null
                    }
                    else {
                        longLatValue = (req.value as String)
                                // remove spaces and degree symbol
                                .replace("\\s".toRegex(), "")
                                .replace("\\u00B0".toRegex(), "")
                                .split(",")
                                // reverse the position of latitude and longitude
                                .reversed()
                                .joinToString(separator = " ") {
                                    when (it.last()) {
                                        'E' -> it.dropLast(1)
                                        'N' -> it.dropLast(1)
                                        else -> "-${it.dropLast(1)}"
                                    }
                                }
                        println(longLatValue)
                        coordinatesValue =
                                "SRID=4326;POINT(${longLatValue})"
                    }

                    util.updateField(
                            token = req.token,
                            table = "sc.languages",
                            column = "coordinates",
                            id = req.id,
                            value = coordinatesValue,
                            cast = "::common.geography",
                    )
            }

//            else -> null
        }

        return ScLanguagesUpdateResponse(ErrorType.NoError)
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