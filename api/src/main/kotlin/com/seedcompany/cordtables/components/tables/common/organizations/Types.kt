package com.seedcompany.cordtables.components.tables.common.organizations

import com.seedcompany.cordtables.common.CommonSensitivity

data class CommonOrganization (
        val id: String? = null,
        val name: String? = null,
        val sensitivity: CommonSensitivity? = null,
        val primary_location: String? = null,
        val created_at: String? = null,
        val created_by: String? = null,
        val modified_at: String? = null,
        val modified_by: String? = null,
        val owning_person: String? = null,
        val owning_group: String? = null,
)

data class CommonOrganizationsInput(
        val name: String? = null,
        val sensitivity: String,
        val primary_location: String? = null,
)
