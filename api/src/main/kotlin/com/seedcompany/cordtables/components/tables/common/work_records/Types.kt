package com.seedcompany.cordtables.components.tables.common.work_records

data class CommonWorkRecords(
    val id: String? = null,
    val person: String? = null,
    val public_full_name: String? = null,
    val ticket: String? = null,
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

data class CommonWorkRecordInput(
    val person: String? = null,
    val ticket: String? = null,
    val hours: Int? = null,
    val minutes: Int? = null,
    val comment:  String? = null,
)

