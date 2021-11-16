package com.seedcompany.cordtables.components.tables.sc.organization_locations

data class organizationLocation(
    var id: Int? = null,

    val organization: Int? = null, // int not null references sc.organizations(id),
    val location: Int? = null, // int not null references sc.locations(id),

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class organizationLocationInput(
    var id: Int? = null,

    val organization: Int? = null, // int not null references sc.organizations(id),
    val location: Int? = null, // int not null references sc.locations(id),

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
