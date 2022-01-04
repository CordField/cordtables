package com.seedcompany.cordtables.components.tables.common.coalition_memberships

data class coalitionMembership(
    var id: String? = null,
    val coalition: String? = null,
    val organization: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class coalitionMembershipInput(
    var id: String? = null,
    val coalition: String? = null,
    val organization: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
