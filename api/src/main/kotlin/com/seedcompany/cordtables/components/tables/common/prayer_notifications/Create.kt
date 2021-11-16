package com.seedcompany.cordtables.components.tables.common.prayer_notifications

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.prayerNotificationInput
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.Read
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPrayerNotificationsCreateRequest(
    val token: String? = null,
    val prayerNotification: prayerNotificationInput,
)

data class CommonPrayerNotificationsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonPrayerNotificationsCreate")
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

    @PostMapping("common-prayer-notifications/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonPrayerNotificationsCreateRequest): CommonPrayerNotificationsCreateResponse {

        // if (req.prayerNotification.name == null) return CommonPrayerNotificationsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.prayer_notifications(request, person,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
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
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.prayerNotification.request,
            req.prayerNotification.person,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return CommonPrayerNotificationsCreateResponse(error = ErrorType.NoError, id = id)
    }

}