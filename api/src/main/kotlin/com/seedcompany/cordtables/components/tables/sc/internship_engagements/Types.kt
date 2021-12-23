package com.seedcompany.cordtables.components.tables.sc.internship_engagements

data class internshipEngagement(
    var id: String? = null,


    val project: Int? = null,
    val ethnologue: Int? = null,
    val change_to_plan: Int? = null,
    val active: Boolean? = null,
    val communications_complete_date: String? = null,
    val complete_date: String? = null,
    val country_of_origin: Int? = null,
    val disbursement_complete_date: String? = null,
    val end_date: String? = null,
    val end_date_override: String? = null,
    val growth_plan: Int? = null,
    val initial_end_date: String? = null,
    val intern: Int? = null,
    val last_reactivated_at: String? = null,
    val mentor: Int? = null,
    val methodology: String? = null, // common.internship_methodology,
    val paratext_registry: String? = null,
    val periodic_reports_directory: Int? = null,
    val position: String? = null, // common.internship_position,
    val start_date: String? = null,
    val start_date_override: String? = null,
    val status: String? = null, // common.engagement_status,

    val group_id: Int? = null,
    val person: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class internshipEngagementInput(
    var id: String? = null,

    val project: Int? = null,
    val ethnologue: Int? = null,
    val change_to_plan: Int? = null,
    val active: Boolean? = null,
    val communications_complete_date: String? = null,
    val complete_date: String? = null,
    val country_of_origin: Int? = null,
    val disbursement_complete_date: String? = null,
    val end_date: String? = null,
    val end_date_override: String? = null,
    val growth_plan: Int? = null,
    val initial_end_date: String? = null,
    val intern: Int? = null,
    val last_reactivated_at: String? = null,
    val mentor: Int? = null,
    val methodology: String? = null, // common.internship_methodology,
    val paratext_registry: String? = null,
    val periodic_reports_directory: Int? = null,
    val position: String? = null, // common.internship_position,
    val start_date: String? = null,
    val start_date_override: String? = null,
    val status: String? = null, // common.engagement_status,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

//project int not null references sc.projects(id),
//ethnologue int not null references sc.ethnologue(id),
//change_to_plan int not null default 1 references sc.change_to_plans(id),
//active bool,
//communications_complete_date timestamp,
//complete_date timestamp,
//country_of_origin int references common.locations(id),
//disbursement_complete_date timestamp,
//end_date timestamp,
//end_date_override timestamp,
//growth_plan int references common.file_versions(id),
//initial_end_date timestamp,
//intern int references admin.people(id),
//last_reactivated_at timestamp,
//mentor int references admin.people(id),
//methodology common.internship_methodology,
//paratext_registry varchar(32),
//periodic_reports_directory int references sc.periodic_reports_directory(id),
//position common.internship_position,
//start_date timestamp,
//start_date_override timestamp,
//status common.engagement_status,
