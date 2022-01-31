package com.seedcompany.cordtables.components.tables.sc.periodic_reports

data class periodicReport(
    var id: String? = null,
    val directory: String? = null,
    val end_at: String? = null,
    val report_file: String? = null,
    val start_at: String? = null,
    val type: String? = null,
    val skipped_reason: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class periodicReportInput(
    var id: String? = null,
    val directory: String? = null,
    val end_at: String? = null,
    val report_file: String? = null,
    val start_at: String? = null,
    val type: String? = null,
    val skipped_reason: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)


