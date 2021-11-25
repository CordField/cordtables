package com.seedcompany.cordtables.components.tables.common.workflows

import com.seedcompany.cordtables.common.CommonTicketStatus

data class CommonWorkflowsRecords(
        val id: Int? = null,
        val ticket_status: CommonTicketStatus? = null,
        val parent: Int? = null,
        val content: String? = null,
        val estimated_total_time: Number? = null,
        val blocked_by: Number? = null,
        val assigned_person: Number ?= null,
        val total_time_worked: Number? = null,
        val comment: String? = null,
        val feedback: String? = null
)
