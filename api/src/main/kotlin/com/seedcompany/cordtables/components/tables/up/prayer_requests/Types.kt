package com.seedcompany.cordtables.components.tables.up.prayer_requests

import javax.security.auth.Subject

data class prayerRequest(
    var id: String? = null,

    val request_language_id:String? = null,
    val target_language_id:String? = null,
    val sensitivity: String? = null,
    val organization_name: String? = null,
    val parent: String? = null,
    val translator: String? = null,
    val location: String? = null,
    val title: String? = null,
    val content: String? = null,
    val reviewed: Boolean? = null,
    val prayer_type: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class prayerRequestInput(
    var id: String? = null,

    val request_language_id:String? = null,
    val target_language_id:String? = null,
    val sensitivity: String? = null,
    val organization_name: String? = null,
    val parent: String? = null,
    val translator: String? = null,
    val location: String? = null,
    val title: String? = null,
    val content: String? = null,
    val reviewed: Boolean? = null,
    val prayer_type: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

