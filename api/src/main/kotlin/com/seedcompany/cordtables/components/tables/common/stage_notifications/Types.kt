package com.seedcompany.cordtables.components.tables.common.stage_notifications

data class stageNotification(
    var id: String? = null,
    val stage: String? = null,
    val on_enter: Boolean? = null,
    val on_exit: Boolean? = null,

    val person: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class stageNotificationInput(
    var id: String? = null,
    val stage: String? = null,
    val on_enter: Boolean? = null,
    val on_exit: Boolean? = null,
    val person: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
