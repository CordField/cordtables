package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

import com.seedcompany.cordtables.common.TableNames

data class roleTablePermission(
    var id: Int? = null,

    val role: Int? = null,
    val table_name: TableNames? = null,
    val table_permission: TablePermissionGrantTypes? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class roleTablePermissionInput(
    var id: Int? = null,

    val role: Int? = null,
    val table_name: String? = null,
    val table_permission: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

enum class TablePermissionGrantTypes{
    Create,
    Delete,
}