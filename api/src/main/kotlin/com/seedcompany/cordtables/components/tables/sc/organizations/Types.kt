package com.seedcompany.cordtables.components.tables.sc.organizations

data class organization(
    var id: Int? = null,

    val neo4j_id: String? = null,
    val address: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class organizationInput(
    var id: Int? = null,
    val neo4j_id: String? = null,
    val address: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

