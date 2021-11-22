package com.seedcompany.cordtables.components.tables.common.prayer_notifications

import com.seedcompany.cordtables.components.tables.common.prayer_notifications.CommonPrayerNotificationsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.CommonPrayerNotificationsUpdateResponse
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.prayerNotificationInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPrayerNotificationsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonPrayerNotificationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonPrayerNotificationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-prayer-notifications/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonPrayerNotificationsUpdateRequest): CommonPrayerNotificationsUpdateResponse {

        if (req.token == null) return CommonPrayerNotificationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonPrayerNotificationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonPrayerNotificationsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "request" -> {
                util.updateField(
                    token = req.token,
                    table = "common.prayer_notifications",
                    column = "request",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.prayer_notifications",
                    column = "person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.prayer_notifications",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.prayer_notifications",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonPrayerNotificationsUpdateResponse(ErrorType.NoError)
    }

}