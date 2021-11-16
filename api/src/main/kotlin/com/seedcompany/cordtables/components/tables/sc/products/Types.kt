package com.seedcompany.cordtables.components.tables.sc.products

import kotlin.collections.List

data class product(
    var id: Int? = null,

    val neo4j_id: String? = null,
    val name: String? = null,
    val change_to_plan: Int? = null,
    val active: Boolean? = null,
    val mediums: Any? = null,
    val methodologies: String? = null,
    val purposes: String? = null,
    val type: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class productInput(
    var id: Int? = null,
    val neo4j_id: String? = null,
    val name: String? = null,
    val change_to_plan: Int? = null,
    val active: Boolean? = null,
    val mediums: List<String> = listOf(),
    val methodologies: String? = null,
    val purposes: String? = null,
    val type: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

enum class ProductMediums{
    A,
    B,
    C
}