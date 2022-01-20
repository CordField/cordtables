package com.seedcompany.cordtables.components.tables.sc.partners

data class partner(
    var id: String? = null,

    val organization: String? = null,
    val active: Boolean? = null,
    val financial_reporting_types: String? = null,
    val is_innovations_client: Boolean? = null,
    val pmc_entity_code: String? = null,
    val point_of_contact: String? = null,
    val types: String? = null,
    val address: String? = null,
    val sensitivity: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class partnerInput(
    var id: String? = null,

    val organization: String? = null,
    val active: Boolean? = null,
    val financial_reporting_types: String? = null,
    val is_innovations_client: Boolean? = null,
    val pmc_entity_code: String? = null,
    val point_of_contact: String? = null,
    val types: String? = null,
    val address: String? = null,
    val sensitivity: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

