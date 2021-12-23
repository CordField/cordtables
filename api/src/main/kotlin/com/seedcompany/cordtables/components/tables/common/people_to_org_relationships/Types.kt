package com.seedcompany.cordtables.components.tables.common.people_to_org_relationships

data class peopleToOrgRelationship(
    var id: String? = null,

    val org: Int? = null,
    val person: Int? = null,
    val relationship_type: String? = null,
    val begin_at: String? = null,
    val end_at: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class peopleToOrgRelationshipInput(
    var id: String? = null,
    val org: Int? = null,
    val person: Int? = null,
    val relationship_type: String? = null,
    val begin_at: String? = null,
    val end_at: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
