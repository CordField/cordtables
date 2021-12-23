package com.seedcompany.cordtables.components.tables.sil.table_of_countries

import software.amazon.ion.Decimal

data class tableOfCountry(
    var id: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val languages: Int? = null,
    val indigenous: Int? = null,
    val established: Int? = null,
    val unestablished: Int? = null,
    val diversity: Float? = null,
    val included: Int? = null,
    val sum_of_populations: Int? = null,
    val mean: Int? = null,
    val median: Int? = null,
    val population: Int? = null,
    val literacy_rate: Float? = null,
    val conventions: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class tableOfCountryInput(
    var id: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val languages: Int? = null,
    val indigenous: Int? = null,
    val established: Int? = null,
    val unestablished: Int? = null,
    val diversity: Float? = null,
    val included: Int? = null,
    val sum_of_populations: Int? = null,
    val mean: Int? = null,
    val median: Int? = null,
    val population: Int? = null,
    val literacy_rate: Float? = null,
    val conventions: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

