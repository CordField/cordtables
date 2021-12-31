package com.seedcompany.cordtables.components.tables.sc.global_partner_performance

data class globalPartnerPerformance(
    var id: String? = null,

    val organization: String? = null,
    val reporting_performance: PartnerPerformanceOptions? = null,
    val financial_performance: PartnerPerformanceOptions? = null,
    val translation_performance: PartnerPerformanceOptions? = null,

    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class globalPartnerPerformanceInput(
    var id: String? = null,
    val organization: String? = null,
    val reporting_performance: String? = null,
    val financial_performance: String? = null,
    val translation_performance: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

enum class PartnerPerformanceOptions {
    `1`,
    `2`,
    `3`,
    `4`
}
