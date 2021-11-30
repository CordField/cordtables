
class CommonNote {
    id?: number | undefined;

    table_name?: string | undefined;
    column_name?: string | undefined;
    row?: number | undefined;
    content?: string | undefined;

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}