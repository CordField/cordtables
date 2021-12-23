package com.seedcompany.cordtables.components.tables.common.stage_notifications

data class stageNotification(
    var id: String? = null,
    val stage: Int? = null,
    val on_enter: Boolean? = null,
    val on_exit: Boolean? = null,

    val person: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class stageNotificationInput(
    var id: String? = null,
    val stage: Int? = null,
    val on_enter: Boolean? = null,
    val on_exit: Boolean? = null,
    val person: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
