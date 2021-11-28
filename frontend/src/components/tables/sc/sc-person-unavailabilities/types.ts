
class ScPersonUnavailability {
    id?: number | undefined;

    person?: number | undefined; // int references admin.people(id),
	period_start?: string | undefined; // timestamp not null,
	period_end?: string | undefined; // timestamp not null,
	description?: string | undefined; // text,

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}