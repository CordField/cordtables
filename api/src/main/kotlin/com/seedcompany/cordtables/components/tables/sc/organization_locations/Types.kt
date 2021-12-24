package com.seedcompany.cordtables.components.tables.sc.organization_locations

data class organizationLocation(
    var id: String? = null,

    val organization: String? = null,
    val location: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class organizationLocationInput(
    var id: String? = null,

    val organization: String? = null,
    val location: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
