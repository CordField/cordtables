package com.seedcompany.cordtables.components.tables.admin.users

data class user(
    var id: String? = null,

    val person: String? = null,
    val email: String? = null,
    val password: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class userInput(
    var id: String? = null,
    val person: String? = null,
    val email: String? = null,
    val password: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
