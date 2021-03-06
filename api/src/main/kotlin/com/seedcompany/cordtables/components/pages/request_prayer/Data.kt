package com.seedcompany.cordtables.components.pages.request_prayer

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.components.tables.up.prayer_requests.prayerRequestInput

data class PrayerRequestData(
    val id: String,

    val request_language_id:String? = null,
    val target_language_id:String? = null,
    val sensitivity: String? = null,
    val organization_name: String? = null,
    val parent: String? = null,
    val translator: String? = null,
    val location: String? = null,
    val title: String? = null,
    val content: String? = null,
    val reviewed: Boolean? = null,
    val prayer_type: String? = null,

    val requestedBy: String?,
    val notify: String?,
    val myRequest: Boolean
)

data class PrayerRequestGetData(
    val id: String,
    val request_language_id:String? = null,
    val target_language_id:String? = null,
    val sensitivity: String? = null,
    val organization_name: String? = null,
    val parent: String? = null,
    val translator: String? = null,
    val location: String? = null,
    val title: String? = null,
    val content: String? = null,
    val reviewed: Boolean? = null,
    val prayer_type: String? = null,
    val created_by: String,
)

data class PrayerRequestUpdateData(
    val id: String,
    val request_language_id:String? = null,
    val target_language_id:String? = null,
    val sensitivity: String? = null,
    val organization_name: String? = null,
    val parent: String? = null,
    val translator: String? = null,
    val location: String? = null,
    val title: String? = null,
    val content: String? = null,
    val reviewed: Boolean? = null,
    val prayer_type: String? = null,
    val created_by: String? = null,
)

data class CommonPrayerRequestsListRequest(
    val token: String?
)

data class CommonPrayerRequestsListResponse(
    val error: ErrorType,
    val prayerRequests: MutableList<PrayerRequestData>?
)

data class CommonPrayerRequestsCreateRequest(
    val token: String? = null,
    val prayerRequest: prayerRequestInput,
)

data class CommonPrayerRequestsUpdateRequest(
    val token: String? = null,
    val prayerRequest: PrayerRequestUpdateData,
)

data class CommonPrayerRequestsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

data class  PrayerRequestNotifyRequest(
    val token: String? = null,
    val action: String? = null,
    val selectedRequest: String? = null
)

data class PrayerRequestGetRequest(
    val token: String? = null,
    val id: String? = null
)

data class PrayerRequestsGetResponse(
    val error: ErrorType,
    val prayerRequest: PrayerRequestGetData?
)
