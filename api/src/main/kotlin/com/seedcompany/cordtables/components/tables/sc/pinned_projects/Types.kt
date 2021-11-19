package com.seedcompany.cordtables.components.tables.sc.pinned_projects

data class pinnedProject(
    var id: Int? = null,

    val person: Int? = null,
    val project: Int? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class pinnedProjectInput(
    var id: Int? = null,

    val person: Int? = null,
    val project: Int? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
