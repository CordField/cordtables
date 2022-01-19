package com.seedcompany.cordtables.components.tables.common.stage_notifications

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_notifications.stageNotificationInput
import com.seedcompany.cordtables.components.tables.common.stage_notifications.Read
import com.seedcompany.cordtables.components.tables.common.stage_notifications.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageNotificationsCreateRequest(
    val token: String? = null,
    val stageNotification: stageNotificationInput,
)

data class CommonStageNotificationsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageNotificationsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common/stage-notifications/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonStageNotificationsCreateRequest): CommonStageNotificationsCreateResponse {

        // if (req.stageNotification.name == null) return CommonStageNotificationsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.stage_notifications(stage, on_enter, on_exit, person,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::BOOLEAN,
                    ?::BOOLEAN,
                    ?,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.stageNotification.stage,
            req.stageNotification.on_enter,
            req.stageNotification.on_exit,
            req.stageNotification.person,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return CommonStageNotificationsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
