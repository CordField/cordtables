package com.seedcompany.cordtables.components.tables.common.work_estimates

data class CommonWorkEstimates(
    val id: String? = null,
    val ticket: String? = null,
    val person: String? = null,
    val hours : Int? = null,
    val minutes: Int? = null,
    val total_time: Number? = null,
    val comment: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class CommonWorkEstimateInput(
    val ticket: String? = null,
    val person: String? = null,
    val hours: Int? = null,
    val minutes: Int? = null,
    val comment:  String? = null,
)

