
class ScPartner {
    id?: string | undefined;

    organization?: number | undefined; // int not null references sc.organizations(id),
	active?: boolean | undefined; // bool,
	financial_reporting_types?: string | undefined; // sc.financial_reporting_types[],
	is_innovations_client?: boolean | undefined; // bool,
	pmc_entity_code?: string | undefined; // varchar(32),
	point_of_contact?: number | undefined; // int references admin.people(id),
	types?: string | undefined; // sc.partner_types[],

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}