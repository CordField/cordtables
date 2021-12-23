package com.seedcompany.cordtables.components.tables.sc.posts

data class post(
    var id: String? = null,

    val directory: Int? = null,
    val type: String? = null,
    val shareability: String? = null,
    val body: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class postInput(
    var id: String? = null,

    val directory: Int? = null,
    val type: String? = null,
    val shareability: String? = null,
    val body: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

