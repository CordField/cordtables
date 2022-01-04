package com.seedcompany.cordtables.components.tables.common.people_to_org_relationships

data class peopleToOrgRelationship(
    var id: String? = null,

    val org: String? = null,
    val person: String? = null,
    val relationship_type: String? = null,
    val begin_at: String? = null,
    val end_at: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class peopleToOrgRelationshipInput(
    var id: String? = null,
    val org: String? = null,
    val person: String? = null,
    val relationship_type: String? = null,
    val begin_at: String? = null,
    val end_at: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
