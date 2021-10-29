package com.seedcompany.cordtables.components.tables.common.locations

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.CommonSensitivity

data class CommonLocation(
    var id: Int? = null,

    val sensitivity: CommonSensitivity? = null,
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

data class CommonLocationInput(
    var id: Int? = null,

    val sensitivity: CommonSensitivity? = null,
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