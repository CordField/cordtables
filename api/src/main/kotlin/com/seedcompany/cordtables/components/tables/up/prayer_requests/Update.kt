package com.seedcompany.cordtables.components.tables.up.prayer_requests

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class UpPrayerRequestsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class UpPrayerRequestsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerRequestsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("up/prayer-requests/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: UpPrayerRequestsUpdateRequest): UpPrayerRequestsUpdateResponse {

        if (req.token == null) return UpPrayerRequestsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return UpPrayerRequestsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return UpPrayerRequestsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "request_language_id" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_requests",
                    column = "request_language_id",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "target_language_id" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_requests",
                    column = "target_language_id",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "sensitivity" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_requests",
                    column = "sensitivity",
                    id = req.id,
                    value = req.value,
                    cast = "::common.sensitivity"
                )
            }
            "organization_name" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_requests",
                    column = "organization_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "parent" -> {
              util.updateField(
                token = req.token,
                table = "up.prayer_requests",
                column = "parent",
                id = req.id,
                value = req.value,
                cast = "::uuid"
              )
            }
            "translator" -> {
              util.updateField(
                token = req.token,
                table = "up.prayer_requests",
                column = "translator",
                id = req.id,
                value = req.value,
                cast = "::uuid"
              )
            }
            "location" -> {
              util.updateField(
                token = req.token,
                table = "up.prayer_requests",
                column = "location",
                id = req.id,
                value = req.value,
              )
            }
            "title" -> {
              util.updateField(
                token = req.token,
                table = "up.prayer_requests",
                column = "title",
                id = req.id,
                value = req.value,
              )
            }
            "content" -> {
              util.updateField(
                token = req.token,
                table = "up.prayer_requests",
                column = "content",
                id = req.id,
                value = req.value,
              )
            }
            "reviewed" -> {
              util.updateField(
                token = req.token,
                table = "up.prayer_requests",
                column = "reviewed",
                id = req.id,
                value = req.value,
                cast = "::BOOLEAN"
              )
            }
            "prayer_type" -> {
              util.updateField(
                token = req.token,
                table = "up.prayer_requests",
                column = "prayer_type",
                id = req.id,
                value = req.value,
                cast = "::up.prayer_type"
              )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_requests",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "up.prayer_requests",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return UpPrayerRequestsUpdateResponse(ErrorType.NoError)
    }

}
