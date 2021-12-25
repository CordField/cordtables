
class ScPost {
    id?: string | undefined;

    directory?: string | undefined; // int not null references sc.posts_directory(id),
    type?: string | undefined; // sc.post_type not null,
    shareability?: string | undefined; // sc.post_shareability not null,
    body?: string | undefined; // text not null,

    created_at?: string | undefined;
    created_by?: string | undefined;
    modified_at?: string | undefined;
    modified_by?: string | undefined;
    owning_person?: string | undefined;
    owning_group?: string | undefined;
}