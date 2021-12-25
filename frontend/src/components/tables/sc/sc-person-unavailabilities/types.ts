
class ScPersonUnavailability {
    id?: string | undefined;

    person?: string | undefined; // int references admin.people(id),
	period_start?: string | undefined; // timestamp not null,
	period_end?: string | undefined; // timestamp not null,
	description?: string | undefined; // text,

    created_at?: string | undefined;
    created_by?: string | undefined;
    modified_at?: string | undefined;
    modified_by?: string | undefined;
    owning_person?: string | undefined;
    owning_group?: string | undefined;
}