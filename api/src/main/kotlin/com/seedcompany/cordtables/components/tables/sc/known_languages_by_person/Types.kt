package com.seedcompany.cordtables.components.tables.sc.known_languages_by_person

data class knownLanguagesByPerson(
    var id: String? = null,
    val person: String? = null,
    val known_language: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class knownLanguagesByPersonInput(
    var id: String? = null,
    val person: String? = null,
    val known_language: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)


