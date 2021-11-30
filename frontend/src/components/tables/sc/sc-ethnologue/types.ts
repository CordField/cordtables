
class ScEthnologue {
    id?: number | undefined;

    neo4j_id?: string | undefined;
    language_index?: number | undefined;
    code?: string | undefined;
    language_name?: string | undefined;
    population?: number | undefined;
    provisional_code?: string | undefined;
    sensitivity?: string | undefined;

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}

// neo4j_id varchar(32) unique,
// language_index int not null references sil.language_index(id),
// code varchar(32),
// language_name varchar(64), -- override for language_index
// population int,
// provisional_code varchar(32),
// sensitivity common.sensitivity not null default 'High',