package com.seedcompany.cordtables.components.tables.up.prayer_notifications

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.*
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.UpPrayerNotificationsCreateRequest
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class UpPrayerNotificationsCreateReadRequest(
    val token: String? = null,
    val prayerNotification: prayerNotificationInput,
)

data class UpPrayerNotificationsCreateReadResponse(
    val error: ErrorType,
    val prayerNotification: prayerNotification? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerNotificationsCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("up/prayer-notifications/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: UpPrayerNotificationsCreateReadRequest): UpPrayerNotificationsCreateReadResponse {

        val createResponse = create.createHandler(
            UpPrayerNotificationsCreateRequest(
                token = req.token,
                prayerNotification = req.prayerNotification
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return UpPrayerNotificationsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            UpPrayerNotificationsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return UpPrayerNotificationsCreateReadResponse(error = readResponse.error, prayerNotification = readResponse.prayerNotification)
    }
}
