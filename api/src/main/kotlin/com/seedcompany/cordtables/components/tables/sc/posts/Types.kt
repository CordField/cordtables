package com.seedcompany.cordtables.components.tables.sc.posts

data class post(
    var id: String? = null,

    val directory: String? = null,
    val type: String? = null,
    val shareability: String? = null,
    val body: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class postInput(
    var id: String? = null,

    val directory: String? = null,
    val type: String? = null,
    val shareability: String? = null,
    val body: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

