package com.seedcompany.cordtables.components.tables.common.org_chart_position_graph

data class orgChartPositionGraph(
    var id: String? = null,
    val from_position: Int? = null,
    val to_position: Int? = null,
    val relationship_type: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class orgChartPositionGraphInput(
    var id: String? = null,
    val from_position: Int? = null,
    val to_position: Int? = null,
    val relationship_type: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
