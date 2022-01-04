package com.seedcompany.cordtables.components.tables.admin.role_memberships

data class roleMembership(
    var id: String? = null,

    val role: String? = null,
    val person: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class roleMembershipInput(
    var id: String? = null,

    val role: String? = null,
    val person: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
