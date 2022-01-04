package com.seedcompany.cordtables.components.tables.admin.groups

data class group(
    var id: String? = null,
    val name: String? = null,
    val parent_group: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class groupInput(
    var id: String? = null,
    val name: String? = null,
    val parent_group: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

