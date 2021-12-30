
class PrayerRequest {
    id?: string | undefined;

    request_language_id?: string | undefined;
    target_language_id?: string | undefined;
    sensitivity?: string | undefined;
    organization_name?: string | undefined;
    parent?: string | undefined;
    translator?: string | undefined;
    location?: string | undefined;
    title?: string | undefined;
    parentRequest?: string | undefined;
    content?: string | undefined;
    reviewed?: boolean | undefined;

    requestedBy?: string | undefined;
    notify?: number | undefined;
    myRequest?: boolean | undefined;
}

class GetPrayerRequest {
    id?: string | undefined;

    request_language_id?: string | undefined;
    target_language_id?: string | undefined;
    sensitivity?: string | undefined;
    organization_name?: string | undefined;
    parent?: string | undefined;
    translator?: string | undefined;
    location?: string | undefined;
    title?: string | undefined;
    parentRequest?: string | undefined;
    content?: string | undefined;
    reviewed?: boolean | undefined;

    created_by?: string | undefined;
}
