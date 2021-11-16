package com.seedcompany.cordtables.components.tables.sc.known_languages_by_person

data class knownLanguagesByPerson(
    var id: Int? = null,
    val person: Int? = null,
    val known_language: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class knownLanguagesByPersonInput(
    var id: Int? = null,
    val person: Int? = null,
    val known_language: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)


