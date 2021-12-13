package com.seedcompany.cordtables.components.tables.sil.iso_639_3_macrolanguages

data class iso6393Macrolanguage(
    var id: Int? = null,

    val m_id: String? = null,
    val i_id: String? = null,
    val i_status: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class iso6393MacrolanguageInput(
    var id: Int? = null,
    val m_id: String? = null,
    val i_id: String? = null,
    val i_status: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
