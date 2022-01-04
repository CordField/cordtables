package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.CommonTicketStatus

data class CommonTickets(
        val id: String? = null,
        val title: String? = null,
        val ticket_status: CommonTicketStatus? = null,
        val parent: String? = null,
        val content: String? = null,
        val created_at: String? = null,
        val created_by: String? = null,
        val modified_at: String? = null,
        val modified_by: String? = null,
        val owning_person: String? = null,
        val owning_group: String? = null
)

data class CommmonTicketsIdTitles(
  val id: String? = null,
  val title: String? = null
)

data class CommonPeopleNames(
  val id: String? = null,
  val name: String? = null
)

data class CommonCountTicketsQuantity(
  val total: Int? = null
)

data class CommonCountPeopleQuantity(
  val total: Int? = null
)

data class  CommonTicketsInput(
        val title: String? = null,
        val ticket_status: String,
        val parent: String? = null,
        val content: String
)
