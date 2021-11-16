package com.seedcompany.cordtables.components.tables.sc.product_scripture_references

data class productScriptureReference(
    var id: Int? = null,

    val product: Int? = null,
    val scripture_reference: Int? = null,
    val change_to_plan: Int? = null,
    val active: Boolean? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class productScriptureReferenceInput(
    var id: Int? = null,

    val product: Int? = null,
    val scripture_reference: Int? = null,
    val change_to_plan: Int? = null,
    val active: Boolean? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
