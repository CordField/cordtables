package com.seedcompany.cordtables.common

import com.fasterxml.jackson.annotation.JsonFormat
import java.lang.reflect.Field
import java.util.*


// -------------------------------------------------------
// Value
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

data class ValueArray(
    val value: Array<String> = arrayOf<String>(),
)

data class ValueFieldZone(
    val value: FieldZone? = null,
)

data class ValueUser(
    val value: GUser? = null,
)

data class ValueFieldRegion(
    val value: FieldRegion? = null,
)

data class ValueFundingAccount(
    val value: FundingAccount? = null,
)

// -------------------------------------------------------
// Objects
data class Ethnologue(
    val code: ValueString? = null,
    val name: ValueString? = null,
    val population: ValueInt? = null,
    val provisionalCode: ValueString? = null,
    val sensitivity: String? = null,
)

data class FundingAccount(
    val id: String,
    val name: ValueString? = null,
    val accountNumber: ValueString? = null,
)

data class FieldRegion(
    val id: String,
    val name: ValueString? = null,
    val fieldZone: ValueFieldZone? = null,
    val director: ValueUser? = null
)

data class FieldZone(
    val id: String,
    val director: ValueUser? = null,
    val name: ValueString? = null,
)

data class GUser(
    val id: String,
)

data class IsoCountry(
    val numeric: Int? = null,
    val country: String? = null,
    val alpha3: String? = null,
    val alpha2: String? = null,
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
    val tags: ValueArray? = null,
    val presetInventory: ValueBoolean? = null,
    val ethnologue: Ethnologue? = null,
)

data class Location(
    val id: String,
    val name: ValueString? = null,
    val type: ValueString? = null,
    val isoAlpha3: ValueString? = null,
    val isoCountry: IsoCountry? = null,
    val fundingAccount: ValueFundingAccount? = null,
    val defaultFieldRegion: ValueFieldRegion? = null,
)

data class Project (
    val id: String? = null,
)


// -------------------------------------------------------
// Lists
data class Languages(
    val items: List<Language>? = listOf(),
    val total: Int? = null,
)

data class Projects (
    val items: List<Project>? = listOf(),
)

data class Locations(
    val items: List<Location>? = listOf(),
    val total: Int? = null,
)

data class GData (
    val languages: Languages? = null,
    val projects: Projects? = null,
    val locations: Locations? = null,
)

data class GResponse (
    val data: GData? = null,
)