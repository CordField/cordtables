package com.seedcompany.cordtables.components.tables.common.org_chart_position_graph

data class orgChartPositionGraph(
    var id: String? = null,
    val from_position: String? = null,
    val to_position: String? = null,
    val relationship_type: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class orgChartPositionGraphInput(
    var id: String? = null,
    val from_position: String? = null,
    val to_position: String? = null,
    val relationship_type: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
