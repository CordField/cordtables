package com.seedcompany.cordtables.components.tables.sc.locations

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


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScLocationsUpdate")
class Update(
    @Autowired
    val util: Utility,

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

        if (req.location.neo4j_id != null) util.updateField(
            token = req.token,
            table = "sc.locations",
            column = "neo4j_id",
            id = req.location.id!!,
            value = req.location.neo4j_id,
        )

        if (req.location.funding_account != null) util.updateField(
            token = req.token,
            table = "sc.locations",
            column = "name",
            id = req.location.id!!,
            value = req.location.funding_account,
        )

        if (req.location.default_region != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "default_region",
            id = req.location.id!!,
            value = req.location.default_region,
        )

        if (req.location.iso_alpha_3 != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "iso_alpha_3",
            id = req.location.id!!,
            value = req.location.iso_alpha_3,
        )

        if (req.location.name != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "funding_account",
            id = req.location.id!!,
            value = req.location.name,
        )

        if (req.location.type != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "type",
            id = req.location.id!!,
            value = req.location.type,
        )


        if (req.location.owning_person != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "owning_person",
            id = req.location.id!!,
            value = req.location.owning_person,
        )

        if (req.location.owning_group != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "owning_group",
            id = req.location.id!!,
            value = req.location.owning_group,
        )

        if (req.location.peer != null) util.updateField(
            token = req.token,
            table = "sc.languages",
            column = "peer",
            id = req.location.id!!,
            value = req.location.peer,
        )

        return ScLocationsUpdateResponse(ErrorType.NoError)
    }

}
