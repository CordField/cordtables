package com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships

data class budgetRecordsPartnership(
    var id: String? = null,
    val budget_record: String? = null,
    val partnership: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class budgetRecordsPartnershipInput(
    var id: String? = null,
    val budget_record: String? = null,
    val partnership: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
