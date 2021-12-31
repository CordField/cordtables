package com.seedcompany.cordtables.components.tables.up.prayer_notifications

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class UpPrayerNotificationsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class UpPrayerNotificationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerNotificationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("up/prayer-notifications/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: UpPrayerNotificationsUpdateRequest): UpPrayerNotificationsUpdateResponse {

        if (req.token == null) return UpPrayerNotificationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return UpPrayerNotificationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return UpPrayerNotificationsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "request" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_notifications",
                    column = "request",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_notifications",
                    column = "person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_notifications",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_notifications",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return UpPrayerNotificationsUpdateResponse(ErrorType.NoError)
    }

}
