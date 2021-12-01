package com.seedcompany.cordtables.components.tables.sil.iso_639_3_names

data class iso6393Name(
    var id: Int? = null,
    val _id: String? = null,
    val print_name: String? = null,
    val inverted_name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class iso6393NameInput(
    var id: Int? = null,
    val _id: String? = null,
    val print_name: String? = null,
    val inverted_name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)