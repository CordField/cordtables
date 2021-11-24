package com.seedcompany.cordtables.components.tables.sc.budget_records

data class BudgetRecord(
    var id: Int? = null,
    val neo4j_id: String? = null,

    var budget: Int? = null,
    var change_to_plan: Int? = null,
    var active: Boolean? = null,
    var amount: Double? = null,
    var fiscal_year: Int? = null,
    var partnership: Int? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
    val peer: Int? = null,
)

data class BudgetRecordInput(
    var id: Int? = null,
)
