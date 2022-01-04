package com.seedcompany.cordtables.components.tables.sc.partnerships

data class partnership(
    var id: String? = null,

    val project: String? = null,
    val partner: String? = null,
    val change_to_plan: String? = null,
    val active: Boolean? = null,
    val agreement: String? = null,



    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class partnershipInput(
    var id: String? = null,

    val project: String? = null,
    val partner: String? = null,
    val change_to_plan: String? = null,
    val active: Boolean? = null,
    val agreement: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

