package com.seedcompany.cordtables.components.tables.sc.people

data class people(
    var id: String? = null,
    val neo4j_id: String? = null,
    val skills: String? = null,
    val status: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class peopleInput(
    var id: String? = null,
    val neo4j_id: String? = null,
    val skills: String? = null,
    val status: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
