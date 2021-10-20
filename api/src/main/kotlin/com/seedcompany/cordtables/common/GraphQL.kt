package com.seedcompany.cordtables.common

import java.util.*

data class ValueInt(
    val value: Int? = null,
)

data class ValueString(
    val value: String? = null,
)

data class ValueBoolean(
    val value: Boolean? = null,
)

data class ValueDate(
    val value: java.sql.Timestamp? = null,   //TODO: not sure what kind of Date, just going for something at the moment
)

data class EthnologueCode(
    val code: ValueString? = null,
)

data class Ethnologue(
    val code: ValueString? = null,
    val name: ValueString? = null,
    val population: ValueInt? = null,
    val provisionalCode: ValueString? = null,
    val sensitivity: String? = null,
)

data class Language(
    val id: String,
    val name: ValueString? = null,
    val displayName: ValueString? = null,
    val displayNamePronunciation: ValueString? = null,
    val isDialect: ValueBoolean? = null,
    val populationOverride: ValueInt? = null,
    val registryOfDialectsCode: ValueString? = null,
    val leastOfThese: ValueBoolean? = null,
    val leastOfTheseReason: ValueString? = null,
    val signLanguageCode: ValueString? = null,
    val sponsorEstimatedEndDate: ValueDate? = null,
    val sensitivity: String? = null,
    val isSignLanguage: ValueBoolean? = null,
    val hasExternalFirstScripture: ValueBoolean? = null,
    val tags: ValueString? = null,
    val presetInventory: ValueBoolean? = null,
    val ethnologue: Ethnologue? = null,
)

data class Project (
    val id: String? = null,
)

data class Languages(
    val items: List<Language>? = listOf(),
    val total: Int? = null,
)

data class Projects (
    val items: List<Project>? = listOf(),
)

data class GData (
    val languages: Languages? = null,
    val projects: Projects? = null,
)

data class GResponse (
    val data: GData? = null,
)