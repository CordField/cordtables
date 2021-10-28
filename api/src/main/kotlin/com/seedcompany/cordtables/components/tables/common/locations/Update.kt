package com.seedcompany.cordtables.components.tables.common.locations

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonLocationsUpdateRequest(
    val token: String?,
    val location: CommonLocationInput? = null,
)

data class CommonLocationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonLocationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-locations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonLocationsUpdateRequest): CommonLocationsUpdateResponse {

        if (req.token == null) return CommonLocationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.location == null) return CommonLocationsUpdateResponse(ErrorType.MissingId)
        if (req.location.id == null) return CommonLocationsUpdateResponse(ErrorType.MissingId)

        if (req.location.type != null && !enumContains<LocationType>(req.location.type)) {
            return CommonLocationsUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.location.name != null) util.updateField(
            token = req.token,
            table = "common.locations",
            column = "name",
            id = req.location.id!!,
            value = req.location.name,
        )

        if (req.location.sensitivity != null) util.updateField(
            token = req.token,
            table = "common.locations",
            column = "sensitivity",
            id = req.location.id!!,
            value = req.location.sensitivity,
            cast = "::common.sensitivity"
        )

        if (req.location.type != null) util.updateField(
            token = req.token,
            table = "common.locations",
            column = "type",
            id = req.location.id!!,
            value = req.location.type,
            cast = "::common.location_type"
        )


        if (req.location.owning_person != null) util.updateField(
            token = req.token,
            table = "common.locations",
            column = "owning_person",
            id = req.location.id!!,
            value = req.location.owning_person,
        )

        if (req.location.owning_group != null) util.updateField(
            token = req.token,
            table = "common.locations",
            column = "owning_group",
            id = req.location.id!!,
            value = req.location.owning_group,
        )

        if (req.location.peer != null) util.updateField(
            token = req.token,
            table = "common.locations",
            column = "peer",
            id = req.location.id!!,
            value = req.location.peer,
        )

        return CommonLocationsUpdateResponse(ErrorType.NoError)
    }

}
