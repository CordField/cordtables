package com.seedcompany.cordtables.components.tables.globalroles

import org.springframework.stereotype.Component


@Component("GlobalRoleUtil")
class GlobalRoleUtil {
    val nonMutableColumns:List<String> = listOf("id", "modified_at", "created_at", "created_by", "modified_by")
}