package com.seedcompany.cordtables.components.tables.common.stage_graph

data class stageGraph(
    var id: Int? = null,
    val from_stage: Int? = null,
    val to_stage: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class stageGraphInput(
    var id: Int? = null,
    val from_stage: Int? = null,
    val to_stage: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)