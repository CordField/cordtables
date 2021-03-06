package com.seedcompany.cordtables.components.tables.sc.languages

import com.seedcompany.cordtables.common.CommonSensitivity

data class Language(
    var id: String? = null,

    val neo4j_id: String? = null,
    val ethnologue: String? = null,
    val name: String? = null,
    val display_name: String? = null,
    val display_name_pronunciation: String? = null,
    // val tags: String? = null,
    val tags: Array<out Any>? = arrayOf(),
    val preset_inventory: Boolean? = null,
    val is_dialect: Boolean? = null,
    val is_sign_language: Boolean? = null,
    val is_least_of_these: Boolean? = null,
    val least_of_these_reason: String? = null,
    val population_override: Int? = null,
    val registry_of_dialects_code: String? = null,
    val sensitivity: CommonSensitivity? = null,
    val sign_language_code: String? = null,
    val sponsor_estimated_end_date: String? = null,

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
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
    val coordinates: String? = null,
    val coordinates_json: String? = null
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Language

    if (tags != null) {
      if (other.tags == null) return false
      if (!tags.contentEquals(other.tags)) return false
    } else if (other.tags != null) return false

    return true
  }

  override fun hashCode(): Int {
    return tags?.contentHashCode() ?: 0
  }
}

data class LanguageInput(
//    var id: Int? = null,
    val name: String,
    val display_name: String,
    val ethnologue: String,
)

enum class EgidsScale(val value: Float)  {
    `0`(1.0F),
    `1`(1.0F),
    `2`(1.0F),
    `3`(1.0F),
    `4`(0.9F),
    `5`(0.8F),
    `6a`(0.6F),
    `6b`(0.4F),
    `7`(0.3F),
    `8a`(0.2F),
    `8b`(0.1F),
    `9`(0.0F),
    `10`(0.0F),
}

enum class LeastReachedProgressScale(val value: Float)  {
    `1`(1.00F),
    `2`(0.75F),
    `3`(0.50F),
    `4`(0.25F),
    `5`(0.00F),
}

enum class PartnerInterestScale(val value: Float)  {
    NoPartnerInterest(0.00F),
    Some(0.33F),
    Significant(0.66F),
    Considerable(1.00F),
}

enum class MultipleLanguagesLeverageLinguisticScale(val value: Float)  {
    None(0.0F),
    Some(0.2F),
    Significant(0.4F),
    Considerable(0.6F),
    Large(0.8F),
    Vast(1.0F),
}

enum class MultipleLanguagesLeverageJointTrainingScale(val value: Float)  {
    None(0.0F),
    Some(0.2F),
    Significant(0.4F),
    Considerable(0.6F),
    Large(0.8F),
    Vast(1.0F),
}

enum class LangCommIntInLanguageDevelopmentScale(val value: Float) {
    NoInterest(0.00F),
    Some(0.33F),
    Significant(0.67F),
    Considerable(1.00F),
}

enum class LangCommIntInScriptureTranslationScale(val value: Float)  {
    NoInterest(0.00F),
    Some(0.25F),
    ExpressedNeed(0.50F),
    Significant(0.75F),
    Considerable(1.00F),
}

enum class AccessToScriptureInLwcScale(val value: Float)  {
    FullAccess(0.00F),
    VastMajority(0.16F),
    LargeMajority(0.33F),
    Majority(0.50F),
    Significant(0.66F),
    Some(0.83F),
    Few(1.00F),
}

enum class BeginWorkGeoChallengesScale(val value: Float) {
    None(-0.50F),
    VeryDifficult(0.00F),
    Difficult(0.33F),
    Moderate(0.66F),
    Easy(1.00F),
}

enum class BeginWorkRelPolObstaclesScale(val value: Float)  {
    None(0.00F),
    VeryDifficult(0.25F),
    Difficult(0.50F),
    Moderate(0.75F),
    Easy(1.00F),
}
