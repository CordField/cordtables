
class UpPrayerRequest {
    id?: string | undefined;

    request_language_id?: string | undefined;
    target_language_id?: string | undefined;
    sensitivity?: string | undefined;
    organization_name?: string | undefined;
    parent?: string | undefined;
    translator?: string | undefined;
    location?: string | undefined;
    title?: string | undefined;
    content?: string | undefined;
    reviewed?: boolean | undefined;
    prayer_type?: string | undefined;

    created_at?: string | undefined;
    created_by?: string | undefined;
    modified_at?: string | undefined;
    modified_by?: string | undefined;
    owning_person?: string | undefined;
    owning_group?: string | undefined;
}
