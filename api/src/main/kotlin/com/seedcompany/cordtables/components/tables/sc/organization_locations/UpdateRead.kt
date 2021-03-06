package com.seedcompany.cordtables.components.tables.sc.organization_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.organization_locations.ScOrganizationLocationsReadRequest
import com.seedcompany.cordtables.components.tables.sc.organization_locations.ScOrganizationLocationsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.organization_locations.organizationLocation
import com.seedcompany.cordtables.components.tables.sc.organization_locations.organizationLocationInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScOrganizationLocationsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScOrganizationLocationsUpdateReadResponse(
    val error: ErrorType,
    val organizationLocation: organizationLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScOrganizationLocationsUpdateRead")
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
    @PostMapping("sc/organization-locations/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScOrganizationLocationsUpdateReadRequest): ScOrganizationLocationsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScOrganizationLocationsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScOrganizationLocationsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScOrganizationLocationsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScOrganizationLocationsUpdateReadResponse(error = readResponse.error, readResponse.organizationLocation)
    }
}
