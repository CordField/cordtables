package com.seedcompany.cordtables.components.tables.common.people_graph

data class peopleGraph(
    var id: Int? = null,
    val from_person: Int? = null,
    val to_person: Int? = null,
    val rel_type: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class peopleGraphInput(
    var id: Int? = null,
    val from_person: Int? = null,
    val to_person: Int? = null,
    val rel_type: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)