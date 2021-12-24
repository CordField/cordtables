package com.seedcompany.cordtables.components.tables.common.notes

data class note(
    var id: String? = null,

    val table_name: String? = null,
    val column_name: String? = null,
    val row: Int? = null,
    val content: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class noteInput(
    var id: String? = null,
    val table_name: String? = null,
    val column_name: String? = null,
    val row: Int? = null,
    val content: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
