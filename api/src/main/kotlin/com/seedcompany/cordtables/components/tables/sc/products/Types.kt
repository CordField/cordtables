package com.seedcompany.cordtables.components.tables.sc.products

import kotlin.collections.List

data class product(
    var id: String? = null,

    val name: String? = null,
    val change_to_plan: String? = null,
    val active: Boolean? = null,
    val mediums: String? = null,
    val methodology: String? = null,
    val purposes: String? = null,
    val type: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class productInput(
    var id: String? = null,

    val name: String? = null,
    val change_to_plan: String? = null,
    val active: Boolean? = null,
    val mediums: String? = null,
    val methodology: String? = null,
    val purposes: String? = null,
    val type: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

enum class ProductMediums{
    A,
    B,
    C
}
