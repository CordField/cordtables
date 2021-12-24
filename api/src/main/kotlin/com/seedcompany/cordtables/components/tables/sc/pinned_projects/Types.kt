package com.seedcompany.cordtables.components.tables.sc.pinned_projects

data class pinnedProject(
    var id: String? = null,

    val person: String? = null,
    val project: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class pinnedProjectInput(
    var id: String? = null,

    val person: String? = null,
    val project: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
