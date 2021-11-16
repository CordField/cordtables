package com.seedcompany.cordtables.components.tables.sc.organization_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.organization_locations.*
import com.seedcompany.cordtables.components.tables.sc.organization_locations.ScOrganizationLocationsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.organization_locations.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScOrganizationLocationsCreateReadRequest(
    val token: String? = null,
    val organizationLocation: organizationLocationInput,
)

data class ScOrganizationLocationsCreateReadResponse(
    val error: ErrorType,
    val organizationLocation: organizationLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScOrganizationLocationsCreateRead")
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
    @PostMapping("sc-organization-locations/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScOrganizationLocationsCreateReadRequest): ScOrganizationLocationsCreateReadResponse {

        val createResponse = create.createHandler(
            ScOrganizationLocationsCreateRequest(
                token = req.token,
                organizationLocation = req.organizationLocation
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScOrganizationLocationsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScOrganizationLocationsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScOrganizationLocationsCreateReadResponse(error = readResponse.error, organizationLocation = readResponse.organizationLocation)
    }
}