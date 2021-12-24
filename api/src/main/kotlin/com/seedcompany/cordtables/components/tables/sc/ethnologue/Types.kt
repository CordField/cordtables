package com.seedcompany.cordtables.components.tables.sc.ethnologue

data class ethnologue(
    var id: String? = null,
    val neo4j_id: String? = null,
    val language_index: String? = null,
    val code: String? = null,
    val language_name: String? = null,
    val population: Int? = null,
    val provisional_code: String? = null,
    val sensitivity: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class ethnologueInput(
    var id: String? = null,
    val neo4j_id: String? = null,
    val language_index: String? = null,
    val code: String? = null,
    val language_name: String? = null,
    val population: Int? = null,
    val provisional_code: String? = null,
    val sensitivity: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
