package com.seedcompany.cordtables.components.tables.common.notes

data class note(
    var id: String? = null,

    val table_name: String? = null,
    val column_name: String? = null,
    val row: Int? = null,
    val content: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class noteInput(
    var id: String? = null,
    val table_name: String? = null,
    val column_name: String? = null,
    val row: Int? = null,
    val content: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
