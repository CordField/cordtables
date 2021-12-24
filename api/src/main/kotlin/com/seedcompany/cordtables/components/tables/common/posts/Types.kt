package com.seedcompany.cordtables.components.tables.common.posts

data class Post(
        var id: String? = null,
        var thread:String? = null,
        var content:String?= null,
        val created_at: String? = null,
        val created_by: String? = null,
        val modified_at: String? = null,
        val modified_by: String? = null,
        val owning_person: String? = null,
        val owning_group: String? = null,
)


data class PostInput(
        val thread: String,
        val content: String,
)
