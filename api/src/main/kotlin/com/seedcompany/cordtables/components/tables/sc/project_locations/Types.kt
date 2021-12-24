package com.seedcompany.cordtables.components.tables.sc.project_locations

data class projectLocation(
    var id: String? = null,

    val active: Boolean? = null,
    val change_to_plan: String? = null,
    val location: String? = null,
    val project: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class projectLocationInput(
    var id: String? = null,
    val active: Boolean? = null,
    val change_to_plan: String? = null,
    val location: String? = null,
    val project: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
