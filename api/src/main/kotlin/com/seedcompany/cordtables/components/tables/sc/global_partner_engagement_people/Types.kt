package com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people

import com.seedcompany.cordtables.common.PeopleToOrgRelationshipType

data class globalPartnerEngagementPeople(
    var id: String? = null,
    val engagement: String? = null,
    val person: String? = null,
    val role: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class globalPartnerEngagementPeopleInput(
    var id: String? = null,
    val engagement: String? = null,
    val person: String? = null,
    val role: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
