
class UpPrayerRequest {
    id?: number | undefined;

    request_language_id?: number | undefined;
    target_language_id?: number | undefined;
    sensitivity?: string | undefined;
    organization_name?: string | undefined;
    parent?: number | undefined;
    translator?: number | undefined;
    location?: string | undefined;
    title?: string | undefined;
    content?: string | undefined;
    reviewed?: boolean | undefined;
    prayer_type?: string | undefined;

    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
}
