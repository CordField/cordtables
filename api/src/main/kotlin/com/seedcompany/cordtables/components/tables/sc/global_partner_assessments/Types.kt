package com.seedcompany.cordtables.components.tables.sc.global_partner_assessments

data class globalPartnerAssessment(
    var id: Int? = null,
    val partner: Int? = null,
    val governance_trans: String? = null,
    val director_trans: String? = null,
    val identity_trans: String? = null,
    val growth_trans: String? = null,
    val comm_support_trans: String? = null,
    val systems_trans: String? = null,
    val fin_management_trans: String? = null,
    val hr_trans: String? = null,
    val it_trans: String? = null,
    val program_design_trans: String? = null,
    val tech_translation_trans: String? = null,
    val director_opp: String? = null,
    val financial_management_opp: String? = null,
    val program_design_opp: String? = null,
    val tech_translation_opp: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class globalPartnerAssessmentInput(
    var id: Int? = null,
    val partner: Int? = null,
    val governance_trans: String? = null,
    val director_trans: String? = null,
    val identity_trans: String? = null,
    val growth_trans: String? = null,
    val comm_support_trans: String? = null,
    val systems_trans: String? = null,
    val fin_management_trans: String? = null,
    val hr_trans: String? = null,
    val it_trans: String? = null,
    val program_design_trans: String? = null,
    val tech_translation_trans: String? = null,
    val director_opp: String? = null,
    val financial_management_opp: String? = null,
    val program_design_opp: String? = null,
    val tech_translation_opp: String? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

enum class PartnerMaturityScale{
    Level1,
    Level2,
    Level3,
    Level4,
}

