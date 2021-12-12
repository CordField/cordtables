package com.seedcompany.cordtables.components.tables.sil.table_of_languages

data class tableOfLanguage(
    var id: Int? = null,

    val iso_639: String? = null,
    val language_name: String? = null,
    val uninverted_name: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val region_code: String? = null,
    val region_name: String? = null,
    val area: String? = null,

    val l1_users: Int? = null,
    val digits: Int? = null,
    val all_users: Int? = null,
    val countries: Int? = null,

    val family: String? = null,
    val classification: String? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,

    val egids: String? = null,
    val is_written: String? = null,

    val institutional: Int? = null,
    val developing: Int? = null,
    val vigorous: Int? = null,
    val in_trouble: Int? = null,
    val dying: Int? = null,
    val extinct: Int? = null,


    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class tableOfLanguageInput(
    var id: Int? = null,
    val iso_639: String? = null,
    val language_name: String? = null,
    val uninverted_name: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val region_code: String? = null,
    val region_name: String? = null,
    val area: String? = null,

    val l1_users: Int? = null,
    val digits: Int? = null,
    val all_users: Int? = null,
    val countries: Int? = null,

    val family: String? = null,
    val classification: String? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,

    val egids: String? = null,
    val is_written: String? = null,

    val institutional: Int? = null,
    val developing: Int? = null,
    val vigorous: Int? = null,
    val in_trouble: Int? = null,
    val dying: Int? = null,
    val extinct: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
