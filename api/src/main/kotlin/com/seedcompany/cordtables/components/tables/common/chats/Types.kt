package com.seedcompany.cordtables.components.tables.common.chats

data class Chat(
    var id: Int? = null,

    val channel: Int? = null,
    val content: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
    val peer: Int? = null,
)

data class ChatInput(
    var id: Int? = null,

    val channel: Int? = null,
    val content: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
    val peer: Int? = null,
)
