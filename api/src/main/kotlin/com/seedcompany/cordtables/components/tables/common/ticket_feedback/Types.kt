package com.seedcompany.cordtables.components.tables.common.ticket_feedback

import com.seedcompany.cordtables.common.CommonTicketFeedbackOptions

data class CommonTicketFeedback(
    val id: Int? = null,
    val ticket: Int? = null,
    val stake_holder: Int? = null,
    val feedback: CommonTicketFeedbackOptions? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class CommonTicketFeedbackInput(
    val ticket: Int? = null,
    val stake_holder: Int? = null,
    val feedback: String? = null,
)