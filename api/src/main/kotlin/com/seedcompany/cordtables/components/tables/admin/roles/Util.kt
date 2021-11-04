package com.seedcompany.cordtables.components.tables.admin.roles

import org.springframework.stereotype.Component
import kotlin.collections.List


@Component("RoleUtil")
class RoleUtil {
    val nonMutableColumns:List<String> = listOf("id", "modified_at", "created_at", "created_by", "modified_by")
}