package com.seedcompany.cordtables.components.tables.sc.person_unavailabilities

data class personUnavailability(
    var id: Int? = null,

    val person: Int? = null, // int references admin.people(id),
    val period_start: String? = null, // timestamp not null,
    val period_end: String? = null, // timestamp not null,
    val description: String? = null, // text,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class personUnavailabilityInput(
    var id: Int? = null,

    val person: Int? = null, // int references admin.people(id),
    val period_start: String? = null, // timestamp not null,
    val period_end: String? = null, // timestamp not null,
    val description: String? = null, // text,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

