package com.seedcompany.cordtables.components.tables.sc.funding_accounts

data class fundingAccount(
    var id: String? = null,

//    val neo4j_id: String? = null,
    val account_number: Int? = null,
    val name: String? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class fundingAccountInput(
    var id: String? = null,
//    val neo4j_id: String? = null,
    val account_number: Int? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)
