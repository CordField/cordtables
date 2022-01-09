package com.seedcompany.cordtables.components.pages.partners

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.components.tables.sil.country_codes.countryCode

class Data {

}

data class Partner(
    val  id: String? = null,
    val active: Boolean? = null,
    val financial_reporting_types: String? = null,
    val is_innovations_client: String? = null,
    val pmc_entity_code: String? = null,
    val point_of_contact: String? = null,
    val types: String? = null,
    val address: String? = null,
    val partner_sensitivity: String? = null,
    val name: String? = null,
    val sensitivity: String? = null,
    val primary_location: String? = null,
    val created_at: String? = null,
    val modified_at: String? = null,

    val gpaid: String? = null,
    val partner: String? = null,
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

    val reporting_performance: String? = null,
    val financial_performance: String? = null,
    val translation_performance: String? = null,
)

data class PartnersRequest(
    val token: String?,
    val id: String? = null,
    val page: Int? = 1,
    val search: String? = null,
    val resultsPerPage: Int? = 50,
)

data class PartnersResponse(
    val error: ErrorType,
    val partners: MutableList<Partner>?,
    val size: Int,
)

data class PartnerReadResponse(
    val error: ErrorType,
    val partner: Partner? = null
)


