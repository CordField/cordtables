package com.seedcompany.cordtables.components.tables.common.threads

data class Thread(
        var id: String? = null,
        var channel:String? = null,
        var content:String?= null,
        val created_at: String? = null,
        val created_by: String? = null,
        val modified_at: String? = null,
        val modified_by: String? = null,
        val owning_person: String? = null,
        val owning_group: String? = null,
)

data class ThreadInput(
        val channel: String,
        val content: String,
)
