package com.seedcompany.cordtables.components.tables.common.files

data class CommonFile(
    var id: String? = null,
    val directory: String? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class CommonFileInput(
    var id: String? = null,
    val directory: String? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
