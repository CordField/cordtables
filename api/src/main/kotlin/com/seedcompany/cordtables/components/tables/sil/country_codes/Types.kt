package com.seedcompany.cordtables.components.tables.sil.country_codes

data class countryCode(
    var id: Int? = null,
    val country: String? = null,
    val name: String? = null,
    val area: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class countryCodeInput(
    var id: Int? = null,
    val country: String? = null,
    val name: String? = null,
    val area: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)