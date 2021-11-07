package com.seedcompany.cordtables.components.tables.admin.group_row_access

import com.seedcompany.cordtables.common.TableNames

data class groupRowAccess(
    var id: Int? = null,

    val group_id: Int? = null,
    val table_name: TableNames? = null,
    val row: Int? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class groupRowAccessInput(
    var id: Int? = null,

    val group_id: Int? = null,
    val table_name: String? = null,
    val row: Int? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)


