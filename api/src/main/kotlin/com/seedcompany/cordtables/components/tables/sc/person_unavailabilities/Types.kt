package com.seedcompany.cordtables.components.tables.sc.person_unavailabilities

data class personUnavailability(
    var id: String? = null,

    val person: String? = null,
    val period_start: String? = null,
    val period_end: String? = null,
    val description: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class personUnavailabilityInput(
    var id: String? = null,

    val person: String? = null,
    val period_start: String? = null,
    val period_end: String? = null,
    val description: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

