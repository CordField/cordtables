package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

import com.seedcompany.cordtables.common.TableNames

data class roleTablePermission(
    var id: String? = null,

    val role: String? = null,
    val table_name: String? = null,
    val table_permission: TablePermissionGrantTypes? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class roleTablePermissionInput(
    var id: String? = null,

    val role: String? = null,
    val table_name: String? = null,
    val table_permission: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

enum class TablePermissionGrantTypes{
    Create,
    Delete,
}
