package com.seedcompany.cordtables.components.tables.common.organizations

import com.seedcompany.cordtables.common.CommonSensitivity

data class CommonOrganization (
        val id: Int? = null,
        val name: String? = null,
        val sensitivity: CommonSensitivity? = null,
        val primary_location: Int? = null,
        val created_at: String? = null,
        val created_by: Int? = null,
        val modified_at: String? = null,
        val modified_by: Int? = null,
        val owning_person: Int? = null,
        val owning_group: Int? = null,
        val peer: Int? = null,
)

data class CommonOrganizationsInput(
        val name: String? = null,
        val sensitivity: String,
        val primary_location: Int? = null,
)