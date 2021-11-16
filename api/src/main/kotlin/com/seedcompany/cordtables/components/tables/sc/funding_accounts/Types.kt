package com.seedcompany.cordtables.components.tables.sc.funding_accounts

data class fundingAccount(
    var id: Int? = null,

    val neo4j_id: String? = null,
    val account_number: Int? = null,
    val name: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class fundingAccountInput(
    var id: Int? = null,
    val neo4j_id: String? = null,
    val account_number: Int? = null,
    val name: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
