
class PrayerRequest {
    id?: number | undefined;

    parent?: number | undefined;
    subject?: string | undefined;
    parentRequest?: number | undefined;
    content?: string | undefined;

    requestedBy?: string | undefined;
    notify?: number | undefined;
    myRequest?: boolean | undefined;
}

class GetPrayerRequest {
    id?: number | undefined;

    parent?: number | undefined;
    subject?: string | undefined;
    content?: string | undefined;

    created_by?: number | undefined;
}