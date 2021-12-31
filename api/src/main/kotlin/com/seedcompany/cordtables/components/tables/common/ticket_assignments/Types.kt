package com.seedcompany.cordtables.components.tables.common.ticket_assignments

data class CommonTicketAssignments(
        val id: String? = null,
        val ticket: String? = null,
        val person: String? = null,
        val created_at: String? = null,
        val created_by: String? = null,
        val modified_at: String? = null,
        val modified_by: String? = null,
        val owning_person: String? = null,
        val owning_group: String? = null,
)

data class CommonTicketAssignmentsInput(
        val ticket: String? = null,
        val person: String? = null,
)

