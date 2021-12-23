package com.seedcompany.cordtables.components.tables.up.prayer_notifications

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.UpPrayerNotificationsReadRequest
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.UpPrayerNotificationsUpdateRequest
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.prayerNotification
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.prayerNotificationInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class UpPrayerNotificationsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class UpPrayerNotificationsUpdateReadResponse(
    val error: ErrorType,
    val prayerNotification: prayerNotification? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerNotificationsUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("up-prayer-notifications/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: UpPrayerNotificationsUpdateReadRequest): UpPrayerNotificationsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            UpPrayerNotificationsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return UpPrayerNotificationsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            UpPrayerNotificationsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return UpPrayerNotificationsUpdateReadResponse(error = readResponse.error, readResponse.prayerNotification)
    }
}
