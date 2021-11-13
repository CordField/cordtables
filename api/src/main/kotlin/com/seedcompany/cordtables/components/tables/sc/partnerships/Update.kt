package com.seedcompany.cordtables.components.tables.sc.partnerships

import com.seedcompany.cordtables.components.tables.sc.partnerships.ScPartnershipsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.partnerships.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.partnerships.ScPartnershipsUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.partnerships.partnershipInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPartnershipsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPartnershipsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScPartnershipsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-partnerships/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScPartnershipsUpdateRequest): ScPartnershipsUpdateResponse {

        if (req.token == null) return ScPartnershipsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScPartnershipsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScPartnershipsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "project" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partnerships",
                    column = "project",
                    id = req.id,
                    value = req.value,
                )
            }
            "partner" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partnerships",
                    column = "partner",
                    id = req.id,
                    value = req.value,
                )
            }
            "change_to_plan" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partnerships",
                    column = "change_to_plan",
                    id = req.id,
                    value = req.value,
                )
            }
            "active" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partnerships",
                    column = "active",
                    id = req.id,
                    value = req.value,
                )
            }
            "agreement" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partnerships",
                    column = "agreement",
                    id = req.id,
                    value = req.value,
                )
            }


            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partnerships",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partnerships",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return ScPartnershipsUpdateResponse(ErrorType.NoError)
    }

}