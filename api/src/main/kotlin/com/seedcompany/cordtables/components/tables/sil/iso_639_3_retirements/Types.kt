package com.seedcompany.cordtables.components.tables.sil.iso_639_3_retirements

data class iso6393Retirement(
    var id: String? = null,

    val _id: String? = null,
    val ref_name: String? = null,
    val ret_reason: String? = null,
    val change_to: String? = null,
    val ret_remedy: String? = null,
    val effective: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class iso6393RetirementInput(
    var id: String? = null,
    val _id: String? = null,
    val ref_name: String? = null,
    val ret_reason: String? = null,
    val change_to: String? = null,
    val ret_remedy: String? = null,
    val effective: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
