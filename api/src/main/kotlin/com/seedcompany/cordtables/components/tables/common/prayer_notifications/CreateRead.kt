package com.seedcompany.cordtables.components.tables.common.prayer_notifications

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.*
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.CommonPrayerNotificationsCreateRequest
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPrayerNotificationsCreateReadRequest(
    val token: String? = null,
    val prayerNotification: prayerNotificationInput,
)

data class CommonPrayerNotificationsCreateReadResponse(
    val error: ErrorType,
    val prayerNotification: prayerNotification? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonPrayerNotificationsCreateRead")
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
    @PostMapping("common-prayer-notifications/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonPrayerNotificationsCreateReadRequest): CommonPrayerNotificationsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonPrayerNotificationsCreateRequest(
                token = req.token,
                prayerNotification = req.prayerNotification
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonPrayerNotificationsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonPrayerNotificationsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonPrayerNotificationsCreateReadResponse(error = readResponse.error, prayerNotification = readResponse.prayerNotification)
    }
}