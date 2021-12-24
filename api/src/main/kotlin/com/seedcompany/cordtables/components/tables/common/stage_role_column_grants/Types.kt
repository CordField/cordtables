package com.seedcompany.cordtables.components.tables.common.stage_role_column_grants

data class stageRoleColumnGrant(
    var id: String? = null,

    val stage: String? = null,
    val role: String? = null,
    val table_name: String? = null,
    val column_name: String? = null,
    val access_level: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class stageRoleColumnGrantInput(
    var id: String? = null,
    val stage: String? = null,
    val role: String? = null,
    val table_name: String? = null,
    val column_name: String? = null,
    val  access_level: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
