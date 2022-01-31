package com.seedcompany.cordtables.components.tables.sc.project_members

data class projectMember(
    var id: String? = null,
    val project: String? = null,
    val person: String? = null,
    val group_id: String? = null,
    val role: String? = null,
    val sensitivity: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class projectMemberInput(
    var id: String? = null,
    val project: String? = null,
    val person: String? = null,
    val group_id: String? = null,
    val role: String? = null,
    val sensitivity: String? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

