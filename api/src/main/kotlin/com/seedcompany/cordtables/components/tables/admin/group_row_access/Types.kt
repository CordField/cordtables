package com.seedcompany.cordtables.components.tables.admin.group_row_access

import com.seedcompany.cordtables.common.TableNames

data class groupRowAccess(
    var id: String? = null,

    val group_id: String? = null,
    val table_name: String? = null,
    val row: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class groupRowAccessInput(
    var id: String? = null,

    val group_id: String? = null,
    val table_name: String? = null,
    val row: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)


