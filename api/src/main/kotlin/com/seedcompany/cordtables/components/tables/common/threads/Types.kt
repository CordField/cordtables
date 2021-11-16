package com.seedcompany.cordtables.components.tables.common.threads

data class Thread(
        var id: Int? = null,
        var channel:Int? = null,
        var content:String?= null,
        val created_at: String? = null,
        val created_by: Int? = null,
        val modified_at: String? = null,
        val modified_by: Int? = null,
        val owning_person: Int? = null,
        val owning_group: Int? = null,
)

data class ThreadInput(
        val channel: Int,
        val content: String,
)