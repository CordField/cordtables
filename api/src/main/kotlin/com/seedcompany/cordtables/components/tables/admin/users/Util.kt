package com.seedcompany.cordtables.components.tables.admin.users

import org.springframework.stereotype.Component
import kotlin.collections.List

@Component("AdminUserUtil")
class AdminUserUtil {
    val nonMutableColumns: List<String> = listOf("id", "modified_at", "created_at", "created_by", "modified_by")
}