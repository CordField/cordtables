package com.seedcompany.cordtables.components.tables.common.ticket_graph


data class CommonTicketGraph(
        val id: Int? = null,
        val from_ticket: Int? = null,
        val to_ticket: Int? = null,
        val created_at: String? = null,
        val created_by: Int? = null,
        val modified_at: String? = null,
        val modified_by: Int? = null,
        val owning_person: Int? = null,
        val owning_group: Int? = null,
)

data class CommonTicketGraphInput(
        val from_ticket: Int? = null,
        val to_ticket: Int? = null,
)
