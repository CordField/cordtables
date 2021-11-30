package com.seedcompany.cordtables.components.tables.sc.organization_locations

import com.seedcompany.cordtables.components.tables.sc.organization_locations.ScOrganizationLocationsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.organization_locations.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.organization_locations.ScOrganizationLocationsUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.organization_locations.organizationLocationInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScOrganizationLocationsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScOrganizationLocationsUpdateResponse(
    val error: ErrorType,
)



@Controller("ScOrganizationLocationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-organization-locations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScOrganizationLocationsUpdateRequest): ScOrganizationLocationsUpdateResponse {

        if (req.token == null) return ScOrganizationLocationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScOrganizationLocationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScOrganizationLocationsUpdateResponse(ErrorType.MissingId)


        when (req.column) {
            "organization" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.organization_locations",
                    column = "organization",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "location" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.organization_locations",
                    column = "location",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.organization_locations",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.organization_locations",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return ScOrganizationLocationsUpdateResponse(ErrorType.NoError)
    }

}