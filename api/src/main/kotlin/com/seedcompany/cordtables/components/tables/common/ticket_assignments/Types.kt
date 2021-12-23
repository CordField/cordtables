package com.seedcompany.cordtables.components.tables.common.ticket_assignments

data class CommonTicketAssignments(
        val id: String? = null,
        val ticket: Int? = null,
        val person: Int? = null,
        val created_at: String? = null,
        val created_by: Int? = null,
        val modified_at: String? = null,
        val modified_by: Int? = null,
        val owning_person: Int? = null,
        val owning_group: Int? = null,
)

data class CommonTicketAssignmentsInput(
        val ticket: Int? = null,
        val person: Int? = null,
)

