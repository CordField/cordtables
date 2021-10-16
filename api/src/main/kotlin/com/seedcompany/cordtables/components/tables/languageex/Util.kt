package com.seedcompany.cordtables.components.tables.languageex

import org.springframework.stereotype.Component

@Component("LanguageExUtil")
class LanguageExUtil {


    val nonMutableColumns:List<String> = listOf("id", "modified_at", "created_at", "created_by", "modified_by", "egids_value","least_reached_value", "partner_interest_value", "multiple_languages_leverage_linguistic_value", "multiple_languages_leverage_joint_training_value", "lang_comm_int_in_language_development_value", "lang_comm_int_in_scripture_translation_value", "access_to_scripture_in_lwc_value", "begin_work_geo_challenges_value", "begin_work_rel_pol_obstacles_value", "prioritization")

    fun getEgidsValue(egidsLevel: egidsScale): Double {
        return when (egidsLevel) {
            egidsScale.valueOf("0") -> 1.0
            egidsScale.valueOf("1") -> 1.0
            egidsScale.valueOf("2") -> 1.0
            egidsScale.valueOf("3") -> 1.0
            egidsScale.valueOf("4") -> 0.9
            egidsScale.valueOf("5") -> 0.8
            egidsScale.valueOf("6a") -> 0.6
           egidsScale.valueOf("6b") -> 0.4
           egidsScale.valueOf( "7") -> 0.3
            egidsScale.valueOf("8a") -> 0.2
            egidsScale.valueOf("8b") -> 0.1
           egidsScale.valueOf( "9") -> 0.0
            egidsScale.valueOf("10") -> 0.0
            // not sure how to remove else here
            else -> 0.0
        }
    }

    fun getLeastReachedValue(leastReachedProgressJpsLevel: leastReachedProgressScale): Double {
        return when (leastReachedProgressJpsLevel) {
            leastReachedProgressScale.valueOf("1") -> 1.00
            leastReachedProgressScale.valueOf("2") -> 0.75
            leastReachedProgressScale.valueOf("3") -> 0.50
            leastReachedProgressScale.valueOf("4") -> 0.25
            leastReachedProgressScale.valueOf("5") -> 0.00
            else -> 0.00
        }
    }

    fun getPartnerInterestValue(partnerInterestLevel: partnerInterestScale): Double {
        return when (partnerInterestLevel) {
            partnerInterestScale.valueOf("No Partner Interest") -> 0.00
            partnerInterestScale.valueOf("Some") -> 0.33
            partnerInterestScale.valueOf("Significant") -> 0.66
            partnerInterestScale.valueOf("Considerable") -> 1.00
            else -> 1.00
        }
    }

    fun getMultipleLanguagesLeverageLinguisticValue(multipleLanguagesLeverageLinguisticLevel: multipleLanguagesLeverageLinguisticScale): Double {
        return when (multipleLanguagesLeverageLinguisticLevel) {
            multipleLanguagesLeverageLinguisticScale.valueOf("None") -> 0.0
            multipleLanguagesLeverageLinguisticScale.valueOf("Some") -> 0.2
            multipleLanguagesLeverageLinguisticScale.valueOf("Significant") -> 0.4
            multipleLanguagesLeverageLinguisticScale.valueOf("Considerable") -> 0.6
            multipleLanguagesLeverageLinguisticScale.valueOf("Large") -> 0.8
            multipleLanguagesLeverageLinguisticScale.valueOf("Vast") -> 1.0
            else -> 1.0
        }
    }

    fun getMultipleLanguagesLeverageJointTrainingValue(multipleLanguagesLeverageJointTrainingLevel: multipleLanguagesLeverageJointTrainingScale): Double {
        return when (multipleLanguagesLeverageJointTrainingLevel) {
            multipleLanguagesLeverageJointTrainingScale.valueOf("None") -> 0.0
            multipleLanguagesLeverageJointTrainingScale.valueOf("Some") -> 0.2
            multipleLanguagesLeverageJointTrainingScale.valueOf("Significant") -> 0.4
            multipleLanguagesLeverageJointTrainingScale.valueOf("Considerable") -> 0.6
            multipleLanguagesLeverageJointTrainingScale.valueOf("Large") -> 0.8
            multipleLanguagesLeverageJointTrainingScale.valueOf("Vast") -> 1.0
            else -> 1.0
        }
    }

    fun getLangCommIntInLanguageDevelopmentValue(langCommIntInLanguageDevelopmentLevel: langCommIntInLanguageDevelopmentScale): Double {
        return when (langCommIntInLanguageDevelopmentLevel) {
            langCommIntInLanguageDevelopmentScale.valueOf("No Interest") -> 0.00
            langCommIntInLanguageDevelopmentScale.valueOf("Some") -> 0.25
            langCommIntInLanguageDevelopmentScale.valueOf("Expressed Need") -> 0.50
            langCommIntInLanguageDevelopmentScale.valueOf("Significant") -> 0.75
            langCommIntInLanguageDevelopmentScale.valueOf("Considerable") -> 1.00
            else -> 1.00
        }
    }

    fun getLangCommIntInScriptureTranslationValue(langCommIntInScriptureTranslationLevel: langCommIntInScriptureTranslationScale): Double {
        return when (langCommIntInScriptureTranslationLevel) {
            langCommIntInScriptureTranslationScale.valueOf("No Interest") -> 0.00
            langCommIntInScriptureTranslationScale.valueOf("Some") -> 0.25
            langCommIntInScriptureTranslationScale.valueOf("Expressed Need") -> 0.50
            langCommIntInScriptureTranslationScale.valueOf("Significant") -> 0.75
            langCommIntInScriptureTranslationScale.valueOf("Considerable") -> 1.00
            else -> 1.00
        }
    }

    fun getAccessToScriptureInLwcValue(accessToScriptureInLwcLevel: accessToScriptureInLwcScale): Double {
        return when (accessToScriptureInLwcLevel) {
            accessToScriptureInLwcScale.valueOf("Full Access") -> 0.00
            accessToScriptureInLwcScale.valueOf("Vast Majority") -> 0.16
            accessToScriptureInLwcScale.valueOf("Large Majority") -> 0.33
            accessToScriptureInLwcScale.valueOf("Majority") -> 0.50
            accessToScriptureInLwcScale.valueOf("Significant") -> 0.66
            accessToScriptureInLwcScale.valueOf("Some") -> 0.83
            accessToScriptureInLwcScale.valueOf("Few") -> 1.00
            else -> 1.00
        }
    }

    fun getBeginWorkGeoChallengesValue(beginWorkGeoChallengesLevel: beginWorkGeoChallengesScale): Double {
        return when (beginWorkGeoChallengesLevel) {
            beginWorkGeoChallengesScale.valueOf("None") -> -0.50
            beginWorkGeoChallengesScale.valueOf("Very Difficult") -> 0.00
            beginWorkGeoChallengesScale.valueOf("Difficult") -> 0.33
            beginWorkGeoChallengesScale.valueOf("Moderate") -> 0.66
           beginWorkGeoChallengesScale.valueOf( "Easy") -> 1.00
            else -> 1.00
        }
    }

    fun getBeginWorkRelPolObstaclesValue(beginWorkRelPolObstaclesLevel: beginWorkRelPolObstaclesScale): Double {
        return when (beginWorkRelPolObstaclesLevel) {
            beginWorkRelPolObstaclesScale.valueOf("None") -> 0.00
            beginWorkRelPolObstaclesScale.valueOf("Very Difficult") -> 0.25
            beginWorkRelPolObstaclesScale.valueOf("Difficult") -> 0.50
            beginWorkRelPolObstaclesScale.valueOf("Moderate") -> 0.75
            beginWorkRelPolObstaclesScale.valueOf("Easy") -> 1.00
            else -> 1.00
        }
    }

}