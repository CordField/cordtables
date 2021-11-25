
class ScPartnership {
    id?: number | undefined;

    project?: number | undefined; // int not null references sc.projects(id),
    partner?: number | undefined; // int not null references sc.organizations(id),
    change_to_plan?: number | undefined; // int not null default 1 references sc.change_to_plans(id),
    active?: boolean | undefined; // bool,
    agreement?: number | undefined; // int references common.file_versions(id),

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}