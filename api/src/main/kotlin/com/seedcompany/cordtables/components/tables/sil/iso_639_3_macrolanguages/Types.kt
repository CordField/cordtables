package com.seedcompany.cordtables.components.tables.sil.iso_639_3_macrolanguages

data class iso6393Macrolanguage(
    var id: String? = null,

    val m_id: String? = null,
    val i_id: String? = null,
    val i_status: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class iso6393MacrolanguageInput(
    var id: String? = null,
    val m_id: String? = null,
    val i_id: String? = null,
    val i_status: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
