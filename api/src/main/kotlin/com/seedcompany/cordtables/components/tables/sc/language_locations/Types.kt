package com.seedcompany.cordtables.components.tables.sc.language_locations

data class languageLocation(
    var id: String? = null,
    val language: String? = null,
    val location: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class languageLocationInput(
    var id: String? = null,
    val language: String? = null,
    val location: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
