package com.seedcompany.cordtables.components.tables.common.coalition_memberships

data class coalitionMembership(
    var id: String? = null,
    val coalition: Int? = null,
    val organization: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class coalitionMembershipInput(
    var id: String? = null,
    val coalition: Int? = null,
    val organization: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
