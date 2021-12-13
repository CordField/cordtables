package com.seedcompany.cordtables.components.tables.up.prayer_requests

import javax.security.auth.Subject

data class prayerRequest(
    var id: Int? = null,

    val request_language_id:Int? = null,
    val target_language_id:Int? = null,
    val sensitivity: String? = null,
    val organization_name: String? = null,
    val parent: Int? = null,
    val translator: Int? = null,
    val location: String? = null,
    val title: String? = null,
    val content: String? = null,
    val reviewed: Boolean? = null,
    val prayer_type: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class prayerRequestInput(
    var id: Int? = null,

    val request_language_id:Int? = null,
    val target_language_id:Int? = null,
    val sensitivity: String? = null,
    val organization_name: String? = null,
    val parent: Int? = null,
    val translator: Int? = null,
    val location: String? = null,
    val title: String? = null,
    val content: String? = null,
    val reviewed: Boolean? = null,
    val prayer_type: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

