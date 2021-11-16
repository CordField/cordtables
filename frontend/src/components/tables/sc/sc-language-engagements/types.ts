
class ScLanguageEngagement {
    id?: number | undefined;

    neo4j_id?: string | undefined; // varchar(32) not null,
    project?: number | undefined; // int not null references sc.projects(id),
    ethnologue?: number | undefined; // int not null references sil.table_of_languages(id),
    change_to_plan?: number | undefined; // int not null default 1 references sc.change_to_plans(id),
    active?: boolean | undefined; // bool,
    communications_complete_date?: string | undefined; // timestamp,
    complete_date?: string | undefined; // timestamp,
    disbursement_complete_date?: string | undefined; // timestamp,
    end_date?: string | undefined; // timestamp,
    end_date_override?: string | undefined; // timestamp,
    initial_end_date?: string | undefined; // timestamp,
    is_first_scripture?: boolean | undefined; // bool,
    is_luke_partnership?: boolean | undefined; // bool,
    is_sent_printing?: boolean | undefined; // bool,
    last_reactivated_at?: string | undefined; // timestamp,
    paratext_registry?: string | undefined; // varchar(32),
    periodic_reports_directory?: number | undefined; // int references sc.periodic_reports_directory(id),
    pnp?: string | undefined; // varchar(255),
    pnp_file?: number | undefined; // int references common.file_versions(id),
    product_engagement_tag?: string | undefined; // common.project_engagement_tag,
    start_date?: string | undefined; // timestamp,
    start_date_override?: string | undefined; // timestamp,
    status?: string | undefined; // common.engagement_status,

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}