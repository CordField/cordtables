package com.seedcompany.cordtables.components.tables.admin.groups

data class group(
    var id: Int? = null,
    val name: String? = null,
    val parent_group: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class groupInput(
    var id: Int? = null,
    val name: String? = null,
    val parent_group: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

