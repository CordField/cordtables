package com.seedcompany.cordtables.components.tables.sil.language_codes

data class languageCode(
    var id: Int? = null,

    val lang: String? = null,
    val country: String? = null,
    val lang_status: String? = null,
    val name: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class languageCodeInput(
    var id: Int? = null,
    val lang: String? = null,
    val country: String? = null,
    val lang_status: String? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)