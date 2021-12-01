package com.seedcompany.cordtables.components.tables.common.prayer_requests

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.prayer_requests.*
import com.seedcompany.cordtables.components.tables.common.prayer_requests.CommonPrayerRequestsCreateRequest
import com.seedcompany.cordtables.components.tables.common.prayer_requests.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPrayerRequestsCreateReadRequest(
    val token: String? = null,
    val prayerRequest: prayerRequestInput,
)

data class CommonPrayerRequestsCreateReadResponse(
    val error: ErrorType,
    val prayerRequest: prayerRequest? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPrayerRequestsCreateRead")
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
    @PostMapping("common-prayer-requests/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonPrayerRequestsCreateReadRequest): CommonPrayerRequestsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonPrayerRequestsCreateRequest(
                token = req.token,
                prayerRequest = req.prayerRequest
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonPrayerRequestsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonPrayerRequestsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonPrayerRequestsCreateReadResponse(error = readResponse.error, prayerRequest = readResponse.prayerRequest)
    }
}