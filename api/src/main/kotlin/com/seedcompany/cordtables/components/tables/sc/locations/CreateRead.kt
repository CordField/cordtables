package com.seedcompany.cordtables.components.tables.sc.locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLocationsCreateReadRequest(
    val token: String? = null,
    val location: ScLocationInput,
)

data class ScLocationsCreateReadResponse(
    val error: ErrorType,
    val location: ScLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLocationsCreateRead")
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
    @PostMapping("sc/locations/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScLocationsCreateReadRequest): ScLocationsCreateReadResponse {

        val createResponse = create.createHandler(
            ScLocationsCreateRequest(
                token = req.token,
                location = req.location
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScLocationsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScLocationsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScLocationsCreateReadResponse(error = readResponse.error, location = readResponse.location)
    }
}
