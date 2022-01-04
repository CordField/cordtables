package com.seedcompany.cordtables.components.tables.common.people_graph

data class peopleGraph(
    var id: String? = null,
    val from_person: String? = null,
    val to_person: String? = null,
    val rel_type: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class peopleGraphInput(
    var id: String? = null,
    val from_person: String? = null,
    val to_person: String? = null,
    val rel_type: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
