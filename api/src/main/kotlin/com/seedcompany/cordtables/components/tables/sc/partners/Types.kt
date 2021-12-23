package com.seedcompany.cordtables.components.tables.sc.partners

data class partner(
    var id: String? = null,

    val organization: Int? = null, // int not null references sc.organizations(id),
    val active: Boolean? = null,
    val financial_reporting_types: String? = null, // sc.financial_reporting_types[],
    val is_innovations_client: Boolean? = null,
    val pmc_entity_code: String? = null, // varchar(32),
    val point_of_contact: Int? = null, // int references admin.people(id),
    val types: String? = null, // sc.partner_types[],

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class partnerInput(
    var id: String? = null,

    val organization: Int? = null, // int not null references sc.organizations(id),
    val active: Boolean? = null,
    val financial_reporting_types: String? = null, // sc.financial_reporting_types[],
    val is_innovations_client: Boolean? = null,
    val pmc_entity_code: String? = null, // varchar(32),
    val point_of_contact: Int? = null, // int references admin.people(id),
    val types: String? = null, // sc.partner_types[],

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

