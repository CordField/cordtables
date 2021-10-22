package com.seedcompany.cordtables.components.tables.sc.language_ex

import com.seedcompany.cordtables.components.tables.languageex.*

data class LanguageEx(
    var id: Int? = null,
    val language_name: String? = null,
    val iso: String? = null,
    val prioritization: Double? = null,
    val progress_bible: Boolean? = null,
    val location_long: String? = null,
    val island: String? = null,
    val province: String? = null,
    val first_language_population: Int? = null,
    val population_value: Double? = null,
    val egids_level: EgidsScale? = null,
    val egids_value: Double? = null,
    val least_reached_progress_jps_level: LeastReachedProgressScale? = null,
    val least_reached_value: Double? = null,
    val partner_interest_level: PartnerInterestScale? = null,
    val partner_interest_value: Double? = null,
    val partner_interest_description: String? = null,
    val partner_interest_source: String? = null,
    val multiple_languages_leverage_linguistic_level: MultipleLanguagesLeverageLinguisticScale? = null,
    val multiple_languages_leverage_linguistic_value: Double? = null,
    val multiple_languages_leverage_linguistic_description: String? = null,
    val multiple_languages_leverage_linguistic_source: String? = null,
    val multiple_languages_leverage_joint_training_level: MultipleLanguagesLeverageJointTrainingScale? = null,
    val multiple_languages_leverage_joint_training_value: Double? = null,
    val multiple_languages_leverage_joint_training_description: String? = null,
    val multiple_languages_leverage_joint_training_source: String? = null,
    val lang_comm_int_in_language_development_level: LangCommIntInLanguageDevelopmentScale? = null,
    val lang_comm_int_in_language_development_value: Double? = null,
    val lang_comm_int_in_language_development_description: String? = null,
    val lang_comm_int_in_language_development_source: String? = null,
    val lang_comm_int_in_scripture_translation_level: LangCommIntInScriptureTranslationScale? = null,
    val lang_comm_int_in_scripture_translation_value: Double? = null,
    val lang_comm_int_in_scripture_translation_description: String? = null,
    val lang_comm_int_in_scripture_translation_source: String? = null,
    val access_to_scripture_in_lwc_level: AccessToScriptureInLwcScale? = null,
    val access_to_scripture_in_lwc_value: Double? = null,
    val access_to_scripture_in_lwc_description: String? = null,
    val access_to_scripture_in_lwc_source: String? = null,
    val begin_work_geo_challenges_level: BeginWorkGeoChallengesScale? = null,
    val begin_work_geo_challenges_value: Double? = null,
    val begin_work_geo_challenges_description: String? = null,
    val begin_work_geo_challenges_source: String? = null,
    val begin_work_rel_pol_obstacles_level: BeginWorkRelPolObstaclesScale? = null,
    val begin_work_rel_pol_obstacles_value: Double? = null,
    val begin_work_rel_pol_obstacles_description: String? = null,
    val begin_work_rel_pol_obstacles_source: String? = null,
    val suggested_strategies: String? = null,
    val comments: String? = null,
    val chat: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
    val peer: Int? = null,
)

data class LanguageExInput(
    var id: Int? = null,
    val language_name: String? = null,
    val iso: String? = null,
    val prioritization: Double? = null,
    val progress_bible: Boolean? = null,
    val location_long: String? = null,
    val island: String? = null,
    val province: String? = null,
    val first_language_population: Int? = null,
    val population_value: Double? = null,
    val egids_level: String? = null,
    val egids_value: Double? = null,
    val least_reached_progress_jps_level: String? = null,
    val least_reached_value: Double? = null,
    val partner_interest_level: String? = null,
    val partner_interest_value: Double? = null,
    val partner_interest_description: String? = null,
    val partner_interest_source: String? = null,
    val multiple_languages_leverage_linguistic_level: String? = null,
    val multiple_languages_leverage_linguistic_value: Double? = null,
    val multiple_languages_leverage_linguistic_description: String? = null,
    val multiple_languages_leverage_linguistic_source: String? = null,
    val multiple_languages_leverage_joint_training_level: String? = null,
    val multiple_languages_leverage_joint_training_value: Double? = null,
    val multiple_languages_leverage_joint_training_description: String? = null,
    val multiple_languages_leverage_joint_training_source: String? = null,
    val lang_comm_int_in_language_development_level: String? = null,
    val lang_comm_int_in_language_development_value: Double? = null,
    val lang_comm_int_in_language_development_description: String? = null,
    val lang_comm_int_in_language_development_source: String? = null,
    val lang_comm_int_in_scripture_translation_level: String? = null,
    val lang_comm_int_in_scripture_translation_value: Double? = null,
    val lang_comm_int_in_scripture_translation_description: String? = null,
    val lang_comm_int_in_scripture_translation_source: String? = null,
    val access_to_scripture_in_lwc_level: String? = null,
    val access_to_scripture_in_lwc_value: Double? = null,
    val access_to_scripture_in_lwc_description: String? = null,
    val access_to_scripture_in_lwc_source: String? = null,
    val begin_work_geo_challenges_level: String? = null,
    val begin_work_geo_challenges_value: Double? = null,
    val begin_work_geo_challenges_description: String? = null,
    val begin_work_geo_challenges_source: String? = null,
    val begin_work_rel_pol_obstacles_level: String? = null,
    val begin_work_rel_pol_obstacles_value: Double? = null,
    val begin_work_rel_pol_obstacles_description: String? = null,
    val begin_work_rel_pol_obstacles_source: String? = null,
    val suggested_strategies: String? = null,
    val comments: String? = null,
    val chat: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
    val peer: Int? = null,
)


enum class EgidsScale  {
    `0`,
    `1`,
    `2`,
    `3`,
    `4`,
    `5`,
    `6a`,
    `6b`,
    `7`,
    `8a`,
    `8b`,
    `9`,
    `10`
}
enum class LeastReachedProgressScale  {
    `0`,
    `1`,
    `2`,
    `3`,
    `4`,
    `5`
}

enum class PartnerInterestScale  {
    NoPartnerInterest,
    Some,
    Significant,
    Considerable
}

enum class MultipleLanguagesLeverageLinguisticScale  {
    None,
    Some,
    Significant,
    Considerable,
    Large,
    Vast
}

enum class MultipleLanguagesLeverageJointTrainingScale  {
    None,
    Some,
    Significant,
    Considerable,
    Large,
    Vast
}

enum class LangCommIntInLanguageDevelopmentScale  {
    NoInterest,
    Some,
    ExpressedNeed,
    Significant,
    Considerable
}

enum class LangCommIntInScriptureTranslationScale  {
    NoInterest,
    Some,
    ExpressedNeed,
    Significant,
    Considerable
}

enum class AccessToScriptureInLwcScale  {
    FullAccess,
    VastMajority,
    LargeMajority,
    Majority,
    Significant,
    Some,
    Few
}

enum class BeginWorkGeoChallengesScale  {
    None,
    VeryDifficult,
    Difficult,
    Moderate,
    Easy
}

enum class BeginWorkRelPolObstaclesScale  {
    None,
    VeryDifficult,
    Difficult,
    Moderate,
    Easy
}

