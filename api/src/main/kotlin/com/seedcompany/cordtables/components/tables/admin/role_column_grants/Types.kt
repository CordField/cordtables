package com.seedcompany.cordtables.components.tables.admin.role_column_grants

import com.seedcompany.cordtables.common.TableNames
import com.seedcompany.cordtables.common.AccessLevels;

data class roleColumnGrant(
    var id: String? = null,

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

data class roleColumnGrantInput(
    var id: String? = null,

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

