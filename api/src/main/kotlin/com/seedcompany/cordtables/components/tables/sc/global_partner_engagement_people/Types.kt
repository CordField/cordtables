package com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people

import com.seedcompany.cordtables.common.PeopleToOrgRelationshipType

data class globalPartnerEngagementPeople(
    var id: Int? = null,
    val engagement: Int? = null,
    val person: Int? = null,
    val role: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class globalPartnerEngagementPeopleInput(
    var id: Int? = null,
    val engagement: Int? = null,
    val person: Int? = null,
    val role: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
