package com.seedcompany.cordtables.components.tables.common.ticket_graph


data class CommonTicketGraph(
        val id: String? = null,
        val from_ticket: String? = null,
        val to_ticket: String? = null,
        val created_at: String? = null,
        val created_by: String? = null,
        val modified_at: String? = null,
        val modified_by: String? = null,
        val owning_person: String? = null,
        val owning_group: String? = null,
)

data class CommonTicketGraphInput(
        val from_ticket: String? = null,
        val to_ticket: String? = null,
)
