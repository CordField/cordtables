package com.seedcompany.cordtables.components.tables.common.locations

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.CommonSensitivity

data class location(
    var id: String? = null,


    val name: String? = null,
    val sensitivity: String? = null,
    val type: String? = null,
    val iso_alpha3: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class locationInput(
    var id: String? = null,

    val sensitivity: String? = null,
    val name: String? = null,
    val type: String? = null,
    val iso_alpha3: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
