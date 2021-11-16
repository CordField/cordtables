package com.seedcompany.cordtables.components.tables.sc.project_locations

data class projectLocation(
    var id: Int? = null,

    val active: Boolean? = null,
    val change_to_plan: Int? = null,
    val location: Int? = null,
    val project: Int? = null,

    val group_id: Int? = null,
    val person: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class projectLocationInput(
    var id: Int? = null,
    val active: Boolean? = null,
    val change_to_plan: Int? = null,
    val location: Int? = null,
    val project: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)