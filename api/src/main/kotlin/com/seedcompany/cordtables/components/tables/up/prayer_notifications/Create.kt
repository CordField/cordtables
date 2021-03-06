package com.seedcompany.cordtables.components.tables.up.prayer_notifications

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.prayerNotificationInput
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.Read
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class UpPrayerNotificationsCreateRequest(
    val token: String? = null,
    val prayerNotification: prayerNotificationInput,
)

data class UpPrayerNotificationsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerNotificationsCreate")
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

    @PostMapping("up/prayer-notifications/create")
    @ResponseBody
    fun createHandler(@RequestBody req: UpPrayerNotificationsCreateRequest): UpPrayerNotificationsCreateResponse {

        // if (req.prayerNotification.name == null) return UpPrayerNotificationsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into up.prayer_notifications(request, person,  created_by, modified_by, owning_person, owning_group)
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
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.prayerNotification.request,
            req.prayerNotification.person,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

//        req.language.id = id

        return UpPrayerNotificationsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
