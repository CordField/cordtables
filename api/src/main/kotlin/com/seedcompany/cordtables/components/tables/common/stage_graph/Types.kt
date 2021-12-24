package com.seedcompany.cordtables.components.tables.common.stage_graph

data class stageGraph(
    var id: String? = null,
    val from_stage: String? = null,
    val to_stage: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class stageGraphInput(
    var id: String? = null,
    val from_stage: String? = null,
    val to_stage: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
