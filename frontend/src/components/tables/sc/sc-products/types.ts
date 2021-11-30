
class ScProduct {
    id?: number | undefined;

    neo4j_id?: string | undefined;
    name?: string | undefined;
    change_to_plan?: number | undefined;
    active?: boolean | undefined;
    mediums?: string[] | undefined;
    methodologies?: string | undefined;
    purposes?: string | undefined;
    type?: string | undefined;

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}