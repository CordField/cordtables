package com.seedcompany.cordtables.components.tables.common.prayer_notifications

import com.seedcompany.cordtables.components.tables.common.prayer_notifications.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.CommonPrayerNotificationsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonPrayerNotificationsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonPrayerNotificationsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)


@Controller("CommonPrayerNotificationsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-prayer-notifications/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonPrayerNotificationsDeleteRequest): CommonPrayerNotificationsDeleteResponse {

        if (req.token == null) return CommonPrayerNotificationsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.prayer_notifications"))
            return CommonPrayerNotificationsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.prayer_notifications where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return CommonPrayerNotificationsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonPrayerNotificationsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}