package com.seedcompany.cordtables.components.tables.common.locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonLocationsCreateReadRequest(
    val token: String? = null,
    val location: locationInput,
)

data class CommonLocationsCreateReadResponse(
    val error: ErrorType,
    val location: location? = null,
)


@Controller("CommonLocationsCreateRead")
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
    @PostMapping("common-locations/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonLocationsCreateReadRequest): CommonLocationsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonLocationsCreateRequest(
                token = req.token,
                location = req.location
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonLocationsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonLocationsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonLocationsCreateReadResponse(error = readResponse.error, location = readResponse.location)
    }
}
