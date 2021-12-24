package com.seedcompany.cordtables.components.tables.admin.group_memberships

data class groupMembership(
    var id: String? = null,
    val group_id: String? = null,
    val person: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class groupMembershipInput(
    var id: String? = null,
    val group_id: String? = null,
    val person: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
