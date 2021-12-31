package com.seedcompany.cordtables.components.tables.sc.product_scripture_references

data class productScriptureReference(
    var id: String? = null,

    val product: String? = null,
    val scripture_reference: String? = null,
    val change_to_plan: String? = null,
    val active: Boolean? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class productScriptureReferenceInput(
    var id: String? = null,

    val product: String? = null,
    val scripture_reference: String? = null,
    val change_to_plan: String? = null,
    val active: Boolean? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
