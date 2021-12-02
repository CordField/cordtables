package com.seedcompany.cordtables.components.tables.common.prayer_notifications

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.CommonPrayerNotificationsReadRequest
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.CommonPrayerNotificationsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.prayerNotification
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.prayerNotificationInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPrayerNotificationsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonPrayerNotificationsUpdateReadResponse(
    val error: ErrorType,
    val prayerNotification: prayerNotification? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPrayerNotificationsUpdateRead")
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
    @PostMapping("common-prayer-notifications/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonPrayerNotificationsUpdateReadRequest): CommonPrayerNotificationsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonPrayerNotificationsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonPrayerNotificationsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonPrayerNotificationsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonPrayerNotificationsUpdateReadResponse(error = readResponse.error, readResponse.prayerNotification)
    }
}