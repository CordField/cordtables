package com.seedcompany.cordtables.components.pages.partners

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.components.tables.sil.country_codes.countryCode


data class Partner(
  val id: String? = null,
  val active: Boolean? = null,
  val financial_reporting_types: Array<out Any> = arrayOf(),
  val is_innovations_client: Boolean? = null,
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
  val gppid: String? = null,
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
  val engagements: MutableList<GlobalPartnerEngagement>? = mutableListOf()
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Partner

    if (financial_reporting_types != null) {
      if (other.financial_reporting_types == null) return false
      if (!financial_reporting_types.contentEquals(other.financial_reporting_types)) return false
    } else if (other.financial_reporting_types != null) return false

    return true
  }

  override fun hashCode(): Int {
    return financial_reporting_types?.contentHashCode() ?: 0
  }
}

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

data class GlobalPartnerEngagement(
  var id: String? = null,

  val organization: String? = null,
  val type: String? = null,
  val mou_start: String? = null,
  val mou_end: String? = null,
  val sc_roles: Array<out Any> = arrayOf(),
  val partner_roles: Array<out Any> = arrayOf(),

  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GlobalPartnerEngagement

    if (!sc_roles.contentEquals(other.sc_roles)) return false
    if (!partner_roles.contentEquals(other.partner_roles)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = sc_roles.contentHashCode()
    result = 31 * result + partner_roles.contentHashCode()
    return result
  }
}

