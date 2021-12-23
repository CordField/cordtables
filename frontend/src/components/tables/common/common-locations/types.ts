
class CommonLocation {
    id?: string | undefined;

    name?: string | undefined;
    sensitivity?: string | undefined;
    type?: string | undefined;
    iso_alpha3?: string | undefined;

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}

// name varchar(255) unique, -- not null,
// 	sensitivity common.sensitivity not null default 'High',
// 	type common.location_type, -- not null,
// 	iso_alpha3 char(3) unique,