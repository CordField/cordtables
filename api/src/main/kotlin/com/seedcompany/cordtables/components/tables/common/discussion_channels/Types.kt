package com.seedcompany.cordtables.components.tables.common.discussion_channels

data class DiscussionChannel(
    var id: String? = null,

    var name: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null
)

data class DiscussionChannelInput(
    var id: String? = null,

    var name: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null
)
