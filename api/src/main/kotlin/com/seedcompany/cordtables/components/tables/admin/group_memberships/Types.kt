package com.seedcompany.cordtables.components.tables.admin.group_memberships

data class groupMembership(
    var id: String? = null,
    val group_id: Int? = null,
    val person: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class groupMembershipInput(
    var id: String? = null,
    val group_id: Int? = null,
    val person: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
