package com.seedcompany.cordtables.components.tables.common.coalition_memberships

import com.seedcompany.cordtables.components.tables.common.coalition_memberships.CommonCoalitionMembershipsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.CommonCoalitionMembershipsUpdateResponse
import com.seedcompany.cordtables.components.tables.common.coalition_memberships.coalitionMembershipInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCoalitionMembershipsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonCoalitionMembershipsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCoalitionMembershipsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-coalition-memberships/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonCoalitionMembershipsUpdateRequest): CommonCoalitionMembershipsUpdateResponse {

        if (req.token == null) return CommonCoalitionMembershipsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonCoalitionMembershipsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonCoalitionMembershipsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "coalition" -> {
                util.updateField(
                    token = req.token,
                    table = "common.coalition_memberships",
                    column = "coalition",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "organization" -> {
                util.updateField(
                    token = req.token,
                    table = "common.coalition_memberships",
                    column = "organization",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.coalition_memberships",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.coalition_memberships",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonCoalitionMembershipsUpdateResponse(ErrorType.NoError)
    }

}