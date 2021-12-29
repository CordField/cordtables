package com.seedcompany.cordtables.components.tables.sc.locations

import com.seedcompany.cordtables.components.tables.common.locations.Update as CommonUpdate
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

data class ScLocationsUpdateRequest(
    val token: String?,
    val location: ScLocationInput? = null,
)

data class ScLocationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLocationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val commonUpdate: CommonUpdate,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-locations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScLocationsUpdateRequest): ScLocationsUpdateResponse {

        if (req.token == null) return ScLocationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.location == null) return ScLocationsUpdateResponse(ErrorType.MissingId)
        if (req.location.id == null) return ScLocationsUpdateResponse(ErrorType.MissingId)

        if (req.location.type != null && !enumContains<LocationType>(req.location.type)) {
            return ScLocationsUpdateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

//        val updateResponse = commonUpdate.updateHandler(
//            CommonLocationsUpdateRequest(
//                token = req.token,
//                location = CommonLocationInput(
//                    id = req.location.id,
//                    name = req.location.name,
//                    type = req.location.type,
//                    owning_person = req.location.owning_person,
//                    owning_group = req.location.owning_group,
//                ),
//            )
//        )
//
//        if (updateResponse.error != ErrorType.NoError) {
//            return ScLocationsUpdateResponse(updateResponse.error)
//        }



        if (req.location.name != null) util.updateField(
            token = req.token,
            table = "sc.locations",
            column = "name",
            id = req.location.id!!,
            value = req.location.name,
        )

        if (req.location.funding_account != null) util.updateField(
            token = req.token,
            table = "sc.locations",
            column = "funding_account",
            id = req.location.id!!,
            value = req.location.funding_account,
            cast = "::uuid"
        )

        if (req.location.default_region != null) util.updateField(
            token = req.token,
            table = "sc.locations",
            column = "default_region",
            id = req.location.id!!,
            value = req.location.default_region,
            cast = "::uuid"
        )

        if (req.location.iso_alpha_3 != null) util.updateField(
            token = req.token,
            table = "sc.locations",
            column = "iso_alpha_3",
            id = req.location.id!!,
            value = req.location.iso_alpha_3,
        )

        if (req.location.type != null) util.updateField(
            token = req.token,
            table = "sc.locations",
            column = "type",
            id = req.location.id!!,
            value = req.location.type,
            cast = "::common.location_type"
        )

        if (req.location.owning_person != null) util.updateField(
            token = req.token,
            table = "sc.locations",
            column = "owning_person",
            id = req.location.id!!,
            value = req.location.owning_person,
            cast = "::uuid"
        )

        if (req.location.owning_group != null) util.updateField(
            token = req.token,
            table = "sc.locations",
            column = "owning_group",
            id = req.location.id!!,
            value = req.location.owning_group,
            cast = "::uuid"
        )

        return ScLocationsUpdateResponse(ErrorType.NoError)
    }

}
