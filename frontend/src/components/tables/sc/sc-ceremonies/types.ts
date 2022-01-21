
class ScCermony {
    id?: string | undefined;

    internship_engagement?: string | undefined;
    language_engagement?: string | undefined;
    ethnologue?: string | undefined;
    actual_date?: string | undefined;
    estimated_date?: string | undefined;
    is_planned?: boolean | undefined;
    type?: string | undefined;

    created_at?: string | undefined;
    created_by?: string | undefined;
    modified_at?: string | undefined;
    modified_by?: string | undefined;
    owning_person?: string | undefined;
    owning_group?: string | undefined;
}


// internship_engagement uuid references sc.internship_engagements(id),
//   language_engagement uuid references sc.language_engagements(id),
//   ethnologue uuid references sil.table_of_languages(id),
//   actual_date timestamp,
//   estimated_date timestamp,
//   is_planned bool,
//   type common.ceremony_type,