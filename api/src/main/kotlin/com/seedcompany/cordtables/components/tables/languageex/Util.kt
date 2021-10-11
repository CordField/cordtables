package com.seedcompany.cordtables.components.tables.languageex


class Util {
    fun getEgidsValue(egidsLevel: String): Double {
        return when (egidsLevel) {
            "0" -> 1.0
            "1" -> 1.0
            "2" -> 1.0
            "3" -> 1.0
            "4" -> 0.9
            "5" -> 0.8
            "6a" -> 0.6
            "6b" -> 0.4
            "7" -> 0.3
            "8a" -> 0.2
            "8b" -> 0.1
            "9" -> 0.0
            "10" -> 0.0
            // not sure how to remove else here
            else -> 0.0
        }
    }

    fun getLeastReachedValue(leastReachedProgressJpsLevel: String): Double {
        return when (leastReachedProgressJpsLevel) {
            "1" -> 1.00
            "2" -> 0.75
            "3" -> 0.50
            "4" -> 0.25
            "5" -> 0.00
            else -> 0.00
        }
    }

    fun getPartnerInterestValue(partnerInterestLevel: String): Double {
        return when (partnerInterestLevel) {
            "No Partner Interest" -> 0.00
            "Some" -> 0.33
            "Significant" -> 0.66
            "Considerable" -> 1.00
            else -> 1.00
        }
    }

    fun getMultipleLanguagesLeverageLinguisticValue(multipleLanguagesLeverageLinguisticLevel: String): Double {
        return when (multipleLanguagesLeverageLinguisticLevel) {
            "None" -> 0.0
            "Some" -> 0.2
            "Significant" -> 0.4
            "Considerable" -> 0.6
            "Large" -> 0.8
            "Vast" -> 1.0
            else -> 1.0
        }
    }

    fun getMultipleLanguagesLeverageJointTrainingValue(multipleLanguagesLeverageJointTrainingLevel: String): Double {
        return when (multipleLanguagesLeverageJointTrainingLevel) {
            "None" -> 0.0
            "Some" -> 0.2
            "Significant" -> 0.4
            "Considerable" -> 0.6
            "Large" -> 0.8
            "Vast" -> 1.0
            else -> 1.0
        }
    }

    fun getLangCommIntInLanguageDevelopmentValue(langCommIntInLanguageDevelopmentLevel: String): Double {
        return when(langCommIntInLanguageDevelopmentLevel){
            "No Interest" -> 0.00
            "Some" -> 0.25
            "Expressed Need" -> 0.50
            "Significant" -> 0.75
            "Considerable" -> 1.00
            else -> 1.00
        }
    }

    fun getLangCommIntInScriptureTranslationValue(langCommIntInScriptureTranslationLevel: String): Double {
        return when(langCommIntInScriptureTranslationLevel){
            "No Interest" -> 0.00
            "Some" -> 0.25
            "Expressed Need" -> 0.50
            "Significant" -> 0.75
            "Considerable" -> 1.00
            else -> 1.00
        }
    }

    fun getAccessToScriptureInLwcValue(accessToScriptureInLwcLevel: String): Double {
        return when(accessToScriptureInLwcLevel){
            "Full Access" -> 0.00
            "Vast Majority" -> 0.16
            "Large Majority" -> 0.33
            "Majority" -> 0.50
            "Significant" -> 0.66
            "Some" -> 0.83
            "Few" -> 1.00
            else -> 1.00
        }
    }

    fun getBeginWorkGeoChallengesValue(beginWorkGeoChallengesLevel: String): Double {
        return when(beginWorkGeoChallengesLevel){
            "None" -> -0.50
            "Very Difficult" -> 0.00
            "Difficult" -> 0.33
            "Moderate" -> 0.66
            "Easy" -> 1.00
            else -> 1.00
        }
    }

    fun getBeginWorkRelPolObstaclesValue(beginWorkRelPolObstaclesLevel: String): Double {
        return when(beginWorkRelPolObstaclesLevel){
            "None" -> 0.00
            "Very Difficult" -> 0.25
            "Difficult" -> 0.50
            "Moderate" -> 0.75
            "Easy" -> 1.00
            else -> 1.00
        }
    }


}