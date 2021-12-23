package com.seedcompany.cordtables.components.tables.sc.field_zones

data class fieldZone(
    var id: String? = null,
    val neo4j_id: String? = null,
    val director: Int? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class fieldZoneInput(
    var id: String? = null,
    val neo4j_id: String? = null,
    val director: Int? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
