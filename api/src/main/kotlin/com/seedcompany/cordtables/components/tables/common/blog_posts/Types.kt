package com.seedcompany.cordtables.components.tables.common.blog_posts

data class blogPost(
    var id: String? = null,
    val blog: String? = null,
    val content: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class blogPostInput(
    var id: String? = null,
    val blog: String? = null,
    val content: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
