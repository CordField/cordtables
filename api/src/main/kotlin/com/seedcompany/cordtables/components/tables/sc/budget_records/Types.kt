package com.seedcompany.cordtables.components.tables.sc.budget_records

data class BudgetRecord(
    var id: String? = null,
    var budget: String? = null,
    var change_to_plan: String? = null,
    var active: Boolean? = null,
    var amount: Double? = null,
    var fiscal_year: Int? = null,
    var partnership: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null
)

data class BudgetRecordInput(
//    var id: Int? = null,
    var budget: String,
    var change_to_plan: String,
)
