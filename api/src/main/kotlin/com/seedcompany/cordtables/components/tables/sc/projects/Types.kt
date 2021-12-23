package com.seedcompany.cordtables.components.tables.sc.projects

data class project(
    var id: String? = null,

    val neo4j_id: String? = null,
    val name: String? = null,
    val change_to_plan: Int? = null,
    val active: Boolean? = null,
    val department: String? = null,
    val estimated_submission: String? = null,
    val field_region: Int? = null,
    val initial_mou_end: String? = null,
    val marketing_location: Int? = null,
    val mou_start: String? = null,
    val mou_end: String? = null,
    val owning_organization: Int? = null,
    val periodic_reports_directory: Int? = null,
    val posts_directory: Int? = null,
    val primary_location: Int? = null,
    val root_directory: Int? = null,
    val status: String? = null,
    val status_changed_at: String? = null,
    val step: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class projectInput(
    var id: String? = null,
    val neo4j_id: String? = null,
    val name: String? = null,
    val change_to_plan: Int? = null,
    val active: Boolean? = null,
    val department: String? = null,
    val estimated_submission: String? = null,
    val field_region: Int? = null,
    val initial_mou_end: String? = null,
    val marketing_location: Int? = null,
    val mou_start: String? = null,
    val mou_end: String? = null,
    val owning_organization: Int? = null,
    val periodic_reports_directory: Int? = null,
    val posts_directory: Int? = null,
    val primary_location: Int? = null,
    val root_directory: Int? = null,
    val status: String? = null,
    val status_changed_at: String? = null,
    val step: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)
