package com.seedcompany.cordtables.components.tables.common.stage_role_column_grants

data class stageRoleColumnGrant(
    var id: String? = null,

    val stage: Int? = null,
    val role: Int? = null,
    val table_name: String? = null,
    val column_name: String? = null,
    val access_level: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class stageRoleColumnGrantInput(
    var id: String? = null,
    val stage: Int? = null,
    val role: Int? = null,
    val table_name: String? = null,
    val column_name: String? = null,
    val  access_level: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
