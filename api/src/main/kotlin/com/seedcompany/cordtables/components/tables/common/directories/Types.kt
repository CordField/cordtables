package com.seedcompany.cordtables.components.tables.common.directories

data class CommonDirectory(
    var id: Int? = null,
    val parent: Int? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class CommonDirectoryInput(
    var id: Int? = null,
    val parent: Int? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)