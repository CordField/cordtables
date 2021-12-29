package com.seedcompany.cordtables.components.tables.sc.field_regions

data class fieldRegion(
    var id: String? = null,
    val field_zone: String? = null,
//    val neo4j_id: String? = null,
    val director: String? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class fieldRegionInput(
    var id: String? = null,
    val field_zone: String? = null,
//    val neo4j_id: String? = null,
    val director: String? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
