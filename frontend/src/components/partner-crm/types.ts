class MultiSelectData{
    value?: string | undefined;
    type?: string | undefined;
}
class PartnerEngagement{
    id?: string = null;
    organization?: string | undefined;
    type?: object[] | undefined;
    mou_start?: string | undefined;
    mou_end?: string | undefined;
    sc_roles?: MultiSelectData[] = null;
    partner_roles?: MultiSelectData[] = null;
    created_at?: string | undefined;
    created_by?: string | undefined;
    modified_at?: string | undefined;
    modified_by?: string | undefined;
    owning_person?: string | undefined;
    owning_group?: string | undefined;
}

class PartnerDetail {
    id?: string = null;
    active?: boolean = null;
    financial_reporting_types?: MultiSelectData[] = null;
    is_innovations_client?: boolean = null;
    pmc_entity_code?: string = null;
    point_of_contact?: string = null;
    types?: string = null;
    address?: string = null;
    partner_sensitivity?: string = null;
    name?: string = null;
    sensitivity?: string = null;
    primary_location?: string = null;

    gpaid?: string = null;
    gppid?: string = null;
    partner?: string = null;
    governance_trans?: string = null;
    director_trans?: string = null;
    identity_trans?: string = null;
    growth_trans?: string = null;
    comm_support_trans?: string = null;
    systems_trans?: string = null;
    fin_management_trans?: string = null;
    hr_trans?: string = null;
    it_trans?: string = null;
    program_design_trans?: string = null;
    tech_translation_trans?: string = null;
    director_opp?: string = null;
    financial_management_opp?: string = null;
    program_design_opp?: string = null;
    tech_translation_opp?: string = null;

    reporting_performance?: string = null;
    financial_performance?: string = null;
    translation_performance?: string = null;

    created_at?: string = null;
    modified_at?: string = null;
    engagements?: PartnerEngagement[] = null;
}


