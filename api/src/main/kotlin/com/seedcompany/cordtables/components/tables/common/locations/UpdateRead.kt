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

data class CommonLocationsUpdateReadRequest(
    val token: String?,
    val location: CommonLocationInput? = null,
)

data class CommonLocationsUpdateReadResponse(
    val error: ErrorType,
    val location: CommonLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonLocationsUpdateRead")
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
    @PostMapping("common-locations/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonLocationsUpdateReadRequest): CommonLocationsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonLocationsUpdateRequest(
                token = req.token,
                location = req.location,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonLocationsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonLocationsReadRequest(
                token = req.token,
                id = req.location!!.id
            )
        )

        return CommonLocationsUpdateReadResponse(error = readResponse.error, readResponse.location)
    }
}
