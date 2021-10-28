package com.seedcompany.cordtables.components.tables.sc.locations

import com.seedcompany.cordtables.common.LocationType

data class ScLocation(
    var id: Int? = null,

    val neo4j_id: String? = null,
    val default_region: Int? = null,
    val funding_account: Int? = null,
    val iso_alpha_3: String? = null,
    val name: String? = null,
    val type: LocationType? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
    val peer: Int? = null,
)

data class ScLocationInput(
    var id: Int? = null,

    val neo4j_id: String? = null,
    val default_region: Int? = null,
    val funding_account: Int? = null,
    val iso_alpha_3: String? = null,
    val name: String? = null,
    val type: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
    val peer: Int? = null,
)