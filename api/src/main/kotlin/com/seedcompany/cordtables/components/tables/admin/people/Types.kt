package com.seedcompany.cordtables.components.tables.admin.people

data class people(
    var id: String? = null,

    val about: String? = null,
    val phone: String? = null,
    val picture: String? = null,
    val private_first_name: String? = null,
    val private_last_name: String? = null,
    val public_first_name: String? = null,
    val public_last_name: String? = null,
    val primary_location: Int? = null,
    val private_full_name: String? = null,
    val public_full_name: String? = null,
    val sensitivity_clearance: Sensitivities? = null,
    val time_zone: String? = null,
    val title: String? = null,
    val status: String? = null,

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class peopleInput(
    var id: String? = null,

    val about: String? = null,
    val phone: String? = null,
    val picture: String? = null,
    val private_first_name: String? = null,
    val private_last_name: String? = null,
    val public_first_name: String? = null,
    val public_last_name: String? = null,
    val primary_location: Int? = null,
    val private_full_name: String? = null,
    val public_full_name: String? = null,
    val sensitivity_clearance: String? = null,
    val time_zone: String? = null,
    val title: String? = null,
    val status: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

enum class Sensitivities{
    Low,
    Medium,
    High,
}
