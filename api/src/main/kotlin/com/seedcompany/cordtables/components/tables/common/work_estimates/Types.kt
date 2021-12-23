package com.seedcompany.cordtables.components.tables.common.work_estimates

data class CommonWorkEstimates(
    val id: String? = null,
    val person: Int? = null,
    val hours : Int? = null,
    val minutes: Int? = null,
    val total_time: Number? = null,
    val comment: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class CommonWorkEstimateInput(
    val person: Int? = null,
    val hours: Int? = null,
    val minutes: Int? = null,
    val comment:  String? = null,
)

