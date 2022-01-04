package com.seedcompany.cordtables.components.tables.sc.global_partner_transitions

data class globalPartnerTransition(
    var id: String? = null,
    val organization: String? = null,
    val transition_type: String? = null,
    val effective_date: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class globalPartnerTransitionInput(
    var id: String? = null,
    val organization: String? = null,
    val transition_type: String? = null,
    val effective_date: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
