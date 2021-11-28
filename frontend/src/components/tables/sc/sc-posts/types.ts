
class ScPost {
    id?: number | undefined;

    directory?: number | undefined; // int not null references sc.posts_directory(id),
    type?: string | undefined; // sc.post_type not null,
    shareability?: string | undefined; // sc.post_shareability not null,
    body?: string | undefined; // text not null,

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}