package com.seedcompany.cordtables.components.tables.common.posts

data class Post(
        var id: Int? = null,
        var thread:Int? = null,
        var content:String?= null,
        val created_at: String? = null,
        val created_by: Int? = null,
        val modified_at: String? = null,
        val modified_by: Int? = null,
        val owning_person: Int? = null,
        val owning_group: Int? = null,
)


data class PostInput(
        val thread: Int,
        val content: String,
)