package com.seedcompany.cordtables.components.tables.sc.language_engagements

data class languageEngagement(
    var id: Int? = null,



    val neo4j_id: String? = null, // varchar(32) not null,
    val project: Int? = null, // int not null references sc.projects(id),
    val ethnologue: Int? = null, // int not null references sc.ethnologue(id),
    val change_to_plan: Int? = null, // int not null default 1 references sc.change_to_plans(id),
    val active: Boolean? = null, // bool,
    val communications_complete_date: String? = null, // timestamp,
    val complete_date: String? = null, // timestamp,
    val disbursement_complete_date: String? = null, // timestamp,
    val end_date: String? = null, // timestamp,
    val end_date_override: String? = null, // timestamp,
    val initial_end_date: String? = null, // timestamp,
    val is_first_scripture: Boolean? = null, // bool,
    val is_luke_partnership: Boolean? = null, // bool,
    val is_sent_printing: Boolean? = null, // bool,
    val last_reactivated_at: String? = null, // timestamp,
    val paratext_registry: String? = null, // varchar(32),
    val periodic_reports_directory: Int? = null, // int references sc.periodic_reports_directory(id),
    val pnp: String? = null, // varchar(255),
    val pnp_file: Int? = null, // int references common.file_versions(id),
    val product_engagement_tag: String? = null, // common.project_engagement_tag,
    val start_date: String? = null, // timestamp,
    val start_date_override: String? = null, // timestamp,
    val status: String? = null, // common.engagement_status,




    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class languageEngagementInput(
    var id: Int? = null,

    val neo4j_id: String? = null, // varchar(32) not null,
    val project: Int? = null, // int not null references sc.projects(id),
    val ethnologue: Int? = null, // int not null references sc.ethnologue(id),
    val change_to_plan: Int? = null, // int not null default 1 references sc.change_to_plans(id),
    val active: Boolean? = null, // bool,
    val communications_complete_date: String? = null, // timestamp,
    val complete_date: String? = null, // timestamp,
    val disbursement_complete_date: String? = null, // timestamp,
    val end_date: String? = null, // timestamp,
    val end_date_override: String? = null, // timestamp,
    val initial_end_date: String? = null, // timestamp,
    val is_first_scripture: Boolean? = null, // bool,
    val is_luke_partnership: Boolean? = null, // bool,
    val is_sent_printing: Boolean? = null, // bool,
    val last_reactivated_at: String? = null, // timestamp,
    val paratext_registry: String? = null, // varchar(32),
    val periodic_reports_directory: Int? = null, // int references sc.periodic_reports_directory(id),
    val pnp: String? = null, // varchar(255),
    val pnp_file: Int? = null, // int references common.file_versions(id),
    val product_engagement_tag: String? = null, // common.project_engagement_tag,
    val start_date: String? = null, // timestamp,
    val start_date_override: String? = null, // timestamp,
    val status: String? = null, // common.engagement_status,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
