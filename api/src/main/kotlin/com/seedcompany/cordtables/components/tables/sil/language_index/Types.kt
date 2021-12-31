package com.seedcompany.cordtables.components.tables.sil.language_index

data class languageIndex(
    var id: String? = null,
    
    val lang: String? = null,
    val country: String? = null,
    val name_type: String? = null,
    val name: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class languageIndexInput(
    var id: String? = null,
    val lang: String? = null,
    val country: String? = null,
    val name_type: String? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
