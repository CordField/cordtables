package com.seedcompany.cordtables.components.tables.sc.project_memberships

data class projectMembership(
    var id: Int? = null,
    val group_id: Int? = null,
    val person: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class projectMembershipInput(
    var id: Int? = null,
    val group_id: Int? = null,
    val person: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)