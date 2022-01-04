package com.seedcompany.cordtables.components.tables.up.prayer_notifications

data class prayerNotification(
    var id: String? = null,

    val request: String? = null,
    val person: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class prayerNotificationInput(
    var id: String? = null,

    val request: String? = null,
    val person: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
