package com.seedcompany.cordtables.components.tables.sc.project_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_locations.ScProjectLocationsReadRequest
import com.seedcompany.cordtables.components.tables.sc.project_locations.ScProjectLocationsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.project_locations.projectLocation
import com.seedcompany.cordtables.components.tables.sc.project_locations.projectLocationInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectLocationsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProjectLocationsUpdateReadResponse(
    val error: ErrorType,
    val projectLocation: projectLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectLocationsUpdateRead")
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
    @PostMapping("sc-project-locations/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScProjectLocationsUpdateReadRequest): ScProjectLocationsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScProjectLocationsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScProjectLocationsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScProjectLocationsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScProjectLocationsUpdateReadResponse(error = readResponse.error, readResponse.projectLocation)
    }
}