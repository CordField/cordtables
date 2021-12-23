package com.seedcompany.cordtables.components.tables.up.prayer_notifications

import com.seedcompany.cordtables.components.tables.up.prayer_notifications.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.UpPrayerNotificationsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class UpPrayerNotificationsDeleteRequest(
    val id: String,
    val token: String?,
)

data class UpPrayerNotificationsDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerNotificationsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("up-prayer-notifications/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: UpPrayerNotificationsDeleteRequest): UpPrayerNotificationsDeleteResponse {

        if (req.token == null) return UpPrayerNotificationsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "up.prayer_notifications"))
            return UpPrayerNotificationsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from up.prayer_notifications where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)
                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return UpPrayerNotificationsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return UpPrayerNotificationsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
