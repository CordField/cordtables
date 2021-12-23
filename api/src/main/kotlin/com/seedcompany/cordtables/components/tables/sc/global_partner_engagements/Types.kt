package com.seedcompany.cordtables.components.tables.sc.global_partner_engagements

import com.seedcompany.cordtables.common.InvolvementOptions
import com.seedcompany.cordtables.common.GlobalPartnerRoles

data class globalPartnerEngagement(
    var id: String? = null,

    val organization: Int? = null,
    val type: String? = null,
    val mou_start: String? = null,
    val mou_end: String? = null,
    val sc_roles: String? = null,
    val partner_roles: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class globalPartnerEngagementInput(
    var id: String? = null,

    val organization: Int? = null,
    val type: String? = null,
    val mou_start: String? = null,
    val mou_end: String? = null,
    val sc_roles: String? = null,
    val partner_roles: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
