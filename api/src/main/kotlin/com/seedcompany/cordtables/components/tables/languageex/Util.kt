package com.seedcompany.cordtables.components.tables.languageex

import org.springframework.stereotype.Component

@Component("LanguageExUtil")
class LanguageExUtil {


    val nonMutableColumns:List<String> = listOf("id", "modified_at", "created_at", "created_by", "modified_by", "egids_value","least_reached_value", "partner_interest_value", "multiple_languages_leverage_linguistic_value", "multiple_languages_leverage_joint_training_value", "lang_comm_int_in_language_development_value", "lang_comm_int_in_scripture_translation_value", "access_to_scripture_in_lwc_value", "begin_work_geo_challenges_value", "begin_work_rel_pol_obstacles_value", "prioritization")

    fun getEgidsValue(egidsLevel: EgidsScale): Double {
        return when (egidsLevel) {
            EgidsScale.valueOf("0") -> 1.0
            EgidsScale.valueOf("1") -> 1.0
            EgidsScale.valueOf("2") -> 1.0
            EgidsScale.valueOf("3") -> 1.0
            EgidsScale.valueOf("4") -> 0.9
            EgidsScale.valueOf("5") -> 0.8
            EgidsScale.valueOf("6a") -> 0.6
           EgidsScale.valueOf("6b") -> 0.4
           EgidsScale.valueOf( "7") -> 0.3
            EgidsScale.valueOf("8a") -> 0.2
            EgidsScale.valueOf("8b") -> 0.1
           EgidsScale.valueOf( "9") -> 0.0
            EgidsScale.valueOf("10") -> 0.0
            // not sure how to remove else here
            else -> 0.0
        }
    }

    fun getLeastReachedValue(leastReachedProgressJpsLevel: LeastReachedProgressScale): Double {
        return when (leastReachedProgressJpsLevel) {
            LeastReachedProgressScale.valueOf("1") -> 1.00
            LeastReachedProgressScale.valueOf("2") -> 0.75
            LeastReachedProgressScale.valueOf("3") -> 0.50
            LeastReachedProgressScale.valueOf("4") -> 0.25
            LeastReachedProgressScale.valueOf("5") -> 0.00
            else -> 0.00
        }
    }

    fun getPartnerInterestValue(partnerInterestLevel: PartnerInterestScale): Double {
        return when (partnerInterestLevel) {
            PartnerInterestScale.valueOf("No Partner Interest") -> 0.00
            PartnerInterestScale.valueOf("Some") -> 0.33
            PartnerInterestScale.valueOf("Significant") -> 0.66
            PartnerInterestScale.valueOf("Considerable") -> 1.00
            else -> 1.00
        }
    }

    fun getMultipleLanguagesLeverageLinguisticValue(multipleLanguagesLeverageLinguisticLevel: MultipleLanguagesLeverageLinguisticScale): Double {
        return when (multipleLanguagesLeverageLinguisticLevel) {
            MultipleLanguagesLeverageLinguisticScale.valueOf("None") -> 0.0
            MultipleLanguagesLeverageLinguisticScale.valueOf("Some") -> 0.2
            MultipleLanguagesLeverageLinguisticScale.valueOf("Significant") -> 0.4
            MultipleLanguagesLeverageLinguisticScale.valueOf("Considerable") -> 0.6
            MultipleLanguagesLeverageLinguisticScale.valueOf("Large") -> 0.8
            MultipleLanguagesLeverageLinguisticScale.valueOf("Vast") -> 1.0
            else -> 1.0
        }
    }

    fun getMultipleLanguagesLeverageJointTrainingValue(multipleLanguagesLeverageJointTrainingLevel: MultipleLanguagesLeverageJointTrainingScale): Double {
        return when (multipleLanguagesLeverageJointTrainingLevel) {
            MultipleLanguagesLeverageJointTrainingScale.valueOf("None") -> 0.0
            MultipleLanguagesLeverageJointTrainingScale.valueOf("Some") -> 0.2
            MultipleLanguagesLeverageJointTrainingScale.valueOf("Significant") -> 0.4
            MultipleLanguagesLeverageJointTrainingScale.valueOf("Considerable") -> 0.6
            MultipleLanguagesLeverageJointTrainingScale.valueOf("Large") -> 0.8
            MultipleLanguagesLeverageJointTrainingScale.valueOf("Vast") -> 1.0
            else -> 1.0
        }
    }

    fun getLangCommIntInLanguageDevelopmentValue(langCommIntInLanguageDevelopmentLevel: LangCommIntInLanguageDevelopmentScale): Double {
        return when (langCommIntInLanguageDevelopmentLevel) {
            LangCommIntInLanguageDevelopmentScale.valueOf("No Interest") -> 0.00
            LangCommIntInLanguageDevelopmentScale.valueOf("Some") -> 0.25
            LangCommIntInLanguageDevelopmentScale.valueOf("Expressed Need") -> 0.50
            LangCommIntInLanguageDevelopmentScale.valueOf("Significant") -> 0.75
            LangCommIntInLanguageDevelopmentScale.valueOf("Considerable") -> 1.00
            else -> 1.00
        }
    }

    fun getLangCommIntInScriptureTranslationValue(langCommIntInScriptureTranslationLevel: LangCommIntInScriptureTranslationScale): Double {
        return when (langCommIntInScriptureTranslationLevel) {
            LangCommIntInScriptureTranslationScale.valueOf("No Interest") -> 0.00
            LangCommIntInScriptureTranslationScale.valueOf("Some") -> 0.25
            LangCommIntInScriptureTranslationScale.valueOf("Expressed Need") -> 0.50
            LangCommIntInScriptureTranslationScale.valueOf("Significant") -> 0.75
            LangCommIntInScriptureTranslationScale.valueOf("Considerable") -> 1.00
            else -> 1.00
        }
    }

    fun getAccessToScriptureInLwcValue(accessToScriptureInLwcLevel: AccessToScriptureInLwcScale): Double {
        return when (accessToScriptureInLwcLevel) {
            AccessToScriptureInLwcScale.valueOf("Full Access") -> 0.00
            AccessToScriptureInLwcScale.valueOf("Vast Majority") -> 0.16
            AccessToScriptureInLwcScale.valueOf("Large Majority") -> 0.33
            AccessToScriptureInLwcScale.valueOf("Majority") -> 0.50
            AccessToScriptureInLwcScale.valueOf("Significant") -> 0.66
            AccessToScriptureInLwcScale.valueOf("Some") -> 0.83
            AccessToScriptureInLwcScale.valueOf("Few") -> 1.00
            else -> 1.00
        }
    }

    fun getBeginWorkGeoChallengesValue(beginWorkGeoChallengesLevel: BeginWorkGeoChallengesScale): Double {
        return when (beginWorkGeoChallengesLevel) {
            BeginWorkGeoChallengesScale.valueOf("None") -> -0.50
            BeginWorkGeoChallengesScale.valueOf("Very Difficult") -> 0.00
            BeginWorkGeoChallengesScale.valueOf("Difficult") -> 0.33
            BeginWorkGeoChallengesScale.valueOf("Moderate") -> 0.66
           BeginWorkGeoChallengesScale.valueOf( "Easy") -> 1.00
            else -> 1.00
        }
    }

    fun getBeginWorkRelPolObstaclesValue(beginWorkRelPolObstaclesLevel: BeginWorkRelPolObstaclesScale): Double {
        return when (beginWorkRelPolObstaclesLevel) {
            BeginWorkRelPolObstaclesScale.valueOf("None") -> 0.00
            BeginWorkRelPolObstaclesScale.valueOf("Very Difficult") -> 0.25
            BeginWorkRelPolObstaclesScale.valueOf("Difficult") -> 0.50
            BeginWorkRelPolObstaclesScale.valueOf("Moderate") -> 0.75
            BeginWorkRelPolObstaclesScale.valueOf("Easy") -> 1.00
            else -> 1.00
        }
    }

}