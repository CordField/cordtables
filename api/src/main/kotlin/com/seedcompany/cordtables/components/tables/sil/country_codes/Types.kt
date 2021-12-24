package com.seedcompany.cordtables.components.tables.sil.country_codes

data class countryCode(
    var id: String? = null,
    val country: String? = null,
    val name: String? = null,
    val area: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class countryCodeInput(
    var id: String? = null,
    val country: String? = null,
    val name: String? = null,
    val area: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
