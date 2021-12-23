package com.seedcompany.cordtables.components.tables.sc.people

data class people(
    var id: String? = null,
    val neo4j_id: String? = null,
    val skills: String? = null,
    val status: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class peopleInput(
    var id: String? = null,
    val neo4j_id: String? = null,
    val skills: String? = null,
    val status: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
