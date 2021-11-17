package com.seedcompany.cordtables.components.tables.common.prayer_notifications

data class prayerNotification(
    var id: Int? = null,

    val request: Int? = null,
    val person: Int? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class prayerNotificationInput(
    var id: Int? = null,

    val request: Int? = null,
    val person: Int? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)