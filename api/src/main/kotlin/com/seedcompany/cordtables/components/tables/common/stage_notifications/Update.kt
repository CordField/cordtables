package com.seedcompany.cordtables.components.tables.common.stage_notifications

import com.seedcompany.cordtables.components.tables.common.stage_notifications.CommonStageNotificationsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.stage_notifications.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.stage_notifications.CommonStageNotificationsUpdateResponse
import com.seedcompany.cordtables.components.tables.common.stage_notifications.stageNotificationInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageNotificationsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonStageNotificationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageNotificationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/stage-notifications/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonStageNotificationsUpdateRequest): CommonStageNotificationsUpdateResponse {

        if (req.token == null) return CommonStageNotificationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonStageNotificationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonStageNotificationsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "stage" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_notifications",
                    column = "stage",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "on_enter" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_notifications",
                    column = "on_enter",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "on_exit" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_notifications",
                    column = "on_exit",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_notifications",
                    column = "person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_notifications",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_notifications",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonStageNotificationsUpdateResponse(ErrorType.NoError)
    }

}
