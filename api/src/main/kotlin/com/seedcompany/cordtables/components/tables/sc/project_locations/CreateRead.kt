package com.seedcompany.cordtables.components.tables.sc.project_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_locations.*
import com.seedcompany.cordtables.components.tables.sc.project_locations.ScProjectLocationsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.project_locations.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProjectLocationsCreateReadRequest(
    val token: String? = null,
    val projectLocation: projectLocationInput,
)

data class ScProjectLocationsCreateReadResponse(
    val error: ErrorType,
    val projectLocation: projectLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectLocationsCreateRead")
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
    @PostMapping("sc-project-locations/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScProjectLocationsCreateReadRequest): ScProjectLocationsCreateReadResponse {

        val createResponse = create.createHandler(
            ScProjectLocationsCreateRequest(
                token = req.token,
                projectLocation = req.projectLocation
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScProjectLocationsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScProjectLocationsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScProjectLocationsCreateReadResponse(error = readResponse.error, projectLocation = readResponse.projectLocation)
    }
}