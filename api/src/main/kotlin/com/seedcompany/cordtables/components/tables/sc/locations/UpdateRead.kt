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

data class ScLocationsUpdateReadRequest(
    val token: String?,
    val location: ScLocationInput? = null,
)

data class ScLocationsUpdateReadResponse(
    val error: ErrorType,
    val location: ScLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScLocationsUpdateRead")
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
    @PostMapping("sc-locations/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScLocationsUpdateReadRequest): ScLocationsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScLocationsUpdateRequest(
                token = req.token,
                location = req.location,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScLocationsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScLocationsReadRequest(
                token = req.token,
                id = req.location!!.id
            )
        )

        return ScLocationsUpdateReadResponse(error = readResponse.error, readResponse.location)
    }
}
