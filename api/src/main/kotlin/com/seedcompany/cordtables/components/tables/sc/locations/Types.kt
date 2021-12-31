package com.seedcompany.cordtables.components.tables.sc.locations

import com.seedcompany.cordtables.common.LocationType

data class ScLocation(
    var id: String? = null,

    val default_region: String? = null,
    val funding_account: String? = null,
    val iso_alpha_3: String? = null,
    val name: String? = null,
    val type: LocationType? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class ScLocationInput(
    var id: String? = null,

    val default_region: String? = null,
    val funding_account: String? = null,
    val iso_alpha_3: String? = null,
    val name: String? = null,
    val type: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
