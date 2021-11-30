package com.seedcompany.cordtables.components.tables.admin.users

data class user(
    var id: Int? = null,

    val person: Int? = null,
    val email: String? = null,
    val password: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class userInput(
    var id: Int? = null,
    val person: Int? = null,
    val email: String? = null,
    val password: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)