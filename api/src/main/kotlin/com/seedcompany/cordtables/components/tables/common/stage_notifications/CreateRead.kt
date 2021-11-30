package com.seedcompany.cordtables.components.tables.common.stage_notifications

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_notifications.*
import com.seedcompany.cordtables.components.tables.common.stage_notifications.CommonStageNotificationsCreateRequest
import com.seedcompany.cordtables.components.tables.common.stage_notifications.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageNotificationsCreateReadRequest(
    val token: String? = null,
    val stageNotification: stageNotificationInput,
)

data class CommonStageNotificationsCreateReadResponse(
    val error: ErrorType,
    val stageNotification: stageNotification? = null,
)


@Controller("CommonStageNotificationsCreateRead")
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
    @PostMapping("common-stage-notifications/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonStageNotificationsCreateReadRequest): CommonStageNotificationsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonStageNotificationsCreateRequest(
                token = req.token,
                stageNotification = req.stageNotification
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonStageNotificationsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonStageNotificationsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonStageNotificationsCreateReadResponse(error = readResponse.error, stageNotification = readResponse.stageNotification)
    }
}