package com.seedcompany.cordtables.components.tables.sc.posts

data class post(
    var id: Int? = null,

    val directory: Int? = null, // int not null references sc.posts_directory(id),
    val type: String? = null, // sc.post_type not null,
    val shareability: String? = null, // sc.post_shareability not null,
    val body: String? = null, // text not null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class postInput(
    var id: Int? = null,

    val directory: Int? = null, // int not null references sc.posts_directory(id),
    val type: String? = null, // sc.post_type not null,
    val shareability: String? = null, // sc.post_shareability not null,
    val body: String? = null, // text not null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

