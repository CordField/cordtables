package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.CommonTicketStatus

data class CommonTickets(
        val id: Int? = null,
        val title: String? = null,
        val ticket_status: CommonTicketStatus? = null,
        val parent: Int? = null,
        val content: String? = null,
        val created_at: String? = null,
        val created_by: Int? = null,
        val modified_at: String? = null,
        val modified_by: Int? = null,
        val owning_person: Int? = null,
        val owning_group: Int? = null
)

data class CommmonTicketsIdTitles(
  val id: Int? = null,
  val title: String? = null
)

data class CommonPeopleNames(
  val id: Int? = null,
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
        val parent: Int? = null,
        val content: String
)
