package com.seedcompany.cordtables.components.tables.common.ticket_feedback

import com.seedcompany.cordtables.common.CommonTicketFeedbackOptions

data class CommonTicketFeedback(
    val id: String? = null,
    val ticket: String? = null,
    val stake_holder: String? = null,
    val feedback: CommonTicketFeedbackOptions? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class CommonTicketFeedbackInput(
    val ticket: String? = null,
    val stake_holder: String? = null,
    val feedback: String? = null,
)
