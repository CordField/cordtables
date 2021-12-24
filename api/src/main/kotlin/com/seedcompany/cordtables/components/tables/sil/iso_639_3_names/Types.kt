package com.seedcompany.cordtables.components.tables.sil.iso_639_3_names

data class iso6393Name(
    var id: String? = null,
    val _id: String? = null,
    val print_name: String? = null,
    val inverted_name: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class iso6393NameInput(
    var id: String? = null,
    val _id: String? = null,
    val print_name: String? = null,
    val inverted_name: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
