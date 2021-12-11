
class PrayerRequest {
    id?: number | undefined;

    request_language_id?: number | undefined;
    target_language_id?: number | undefined;
    sensitivity?: string | undefined;
    organization_name?: string | undefined;
    parent?: number | undefined;
    translator?: number | undefined;
    location?: string | undefined;
    title?: string | undefined;
    parentRequest?: number | undefined;
    content?: string | undefined;
    reviewed?: boolean | undefined;

    requestedBy?: string | undefined;
    notify?: number | undefined;
    myRequest?: boolean | undefined;
}

class GetPrayerRequest {
    id?: number | undefined;

    request_language_id?: number | undefined;
    target_language_id?: number | undefined;
    sensitivity?: string | undefined;
    organization_name?: string | undefined;
    parent?: number | undefined;
    translator?: number | undefined;
    location?: string | undefined;
    title?: string | undefined;
    parentRequest?: number | undefined;
    content?: string | undefined;
    reviewed?: boolean | undefined;

    created_by?: number | undefined;
}
