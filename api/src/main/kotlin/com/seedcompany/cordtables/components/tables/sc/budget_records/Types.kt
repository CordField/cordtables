package com.seedcompany.cordtables.components.tables.sc.budget_records

data class BudgetRecord(
    val id: String? = null,
    val budget: String? = null,
    val change_to_plan: String? = null,
    val active: Boolean? = null,
    val amount: Double? = null,
    val fiscal_year: Int? = null,
    val organization: String? = null,
    val sensitivity: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null
)

data class BudgetRecordInput(
    val id: String? = null,
    val budget: String? = null,
    val change_to_plan: String? = null,
    val active: Boolean? = null,
    val amount: Double? = null,
    val fiscal_year: Int? = null,
    val organization: String? = null,
    val sensitivity: String? = null,
)

