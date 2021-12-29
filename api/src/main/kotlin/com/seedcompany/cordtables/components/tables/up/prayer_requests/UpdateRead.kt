package com.seedcompany.cordtables.components.tables.up.prayer_requests

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.up.prayer_requests.UpPrayerRequestsReadRequest
import com.seedcompany.cordtables.components.tables.up.prayer_requests.UpPrayerRequestsUpdateRequest
import com.seedcompany.cordtables.components.tables.up.prayer_requests.prayerRequest
import com.seedcompany.cordtables.components.tables.up.prayer_requests.prayerRequestInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class UpPrayerRequestsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class UpPrayerRequestsUpdateReadResponse(
    val error: ErrorType,
    val prayerRequest: prayerRequest? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerRequestsUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("up/prayer-requests/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: UpPrayerRequestsUpdateReadRequest): UpPrayerRequestsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            UpPrayerRequestsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return UpPrayerRequestsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            UpPrayerRequestsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return UpPrayerRequestsUpdateReadResponse(error = readResponse.error, readResponse.prayerRequest)
    }
}
