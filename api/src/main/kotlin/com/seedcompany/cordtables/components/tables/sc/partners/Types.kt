package com.seedcompany.cordtables.components.tables.sc.partners

enum class FinancialReportingTypes{
    `A`,
    `B`,
    `C`
}

enum class PartnerTypes{
    `A`,
    `B`,
    `C`
}

data class Partner(
        val id: Int?,
        val organization: Int?,
        val active: Boolean? = null,
        val financial_reporting_types: Array<FinancialReportingTypes>? = null,
        val is_innovations_client: Boolean? = null,
        val pmc_entity_code: String? = null,
        val point_of_contact: Int? = null,
        val types: Array<PartnerTypes>? = null,
        val created_at: String?,
        val created_by: Int?,
        val modified_at: String?,
        val modified_by: Int?,
        val owning_person: Int?,
        val owning_group: Int?,
        val peer: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Partner

        if (financial_reporting_types != null) {
            if (other.financial_reporting_types == null) return false
            if (!financial_reporting_types.contentEquals(other.financial_reporting_types)) return false
        } else if (other.financial_reporting_types != null) return false
        if (types != null) {
            if (other.types == null) return false
            if (!types.contentEquals(other.types)) return false
        } else if (other.types != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = financial_reporting_types?.contentHashCode() ?: 0
        result = 31 * result + (types?.contentHashCode() ?: 0)
        return result
    }
}

data class PartnerInput(
//      var id: Int? = null,
        val organization: Int
)

