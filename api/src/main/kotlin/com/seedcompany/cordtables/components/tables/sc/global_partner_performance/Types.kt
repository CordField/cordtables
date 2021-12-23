package com.seedcompany.cordtables.components.tables.sc.global_partner_performance

data class globalPartnerPerformance(
    var id: String? = null,

    val organization: Int? = null,
    val reporting_performance: PartnerPerformanceOptions? = null,
    val financial_performance: PartnerPerformanceOptions? = null,
    val translation_performance: PartnerPerformanceOptions? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class globalPartnerPerformanceInput(
    var id: String? = null,
    val organization: Int? = null,
    val reporting_performance: String? = null,
    val financial_performance: String? = null,
    val translation_performance: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

enum class PartnerPerformanceOptions {
    `1`,
    `2`,
    `3`,
    `4`
}
