package com.seedcompany.cordtables.components.tables.admin.role_memberships

data class roleMembership(
    var id: String? = null,

    val role: Int? = null,
    val person: Int? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class roleMembershipInput(
    var id: String? = null,

    val role: Int? = null,
    val person: Int? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
