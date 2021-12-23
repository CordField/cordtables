
class ScProject {
    id?: string | undefined;

    neo4j_id?: string | undefined;
    name?: string | undefined;
    change_to_plan?: number | undefined;
    active?: boolean | undefined;
    department?: string | undefined;
    estimated_submission?: string | undefined;
    field_region?: number | undefined;
    initial_mou_end?: string | undefined;
    marketing_location?: number | undefined;
    mou_start?: string | undefined;
    mou_end?: string | undefined;
    owning_organization?: number | undefined;
    periodic_reports_directory?: number | undefined;
    posts_directory?: number | undefined;
    primary_location?: number | undefined;
    root_directory?: number | undefined;
    status?: string | undefined;
    status_changed_at?: string | undefined;
    step?: string | undefined;

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}