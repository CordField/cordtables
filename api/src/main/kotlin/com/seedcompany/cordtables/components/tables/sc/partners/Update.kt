package com.seedcompany.cordtables.components.tables.sc.partners

import com.seedcompany.cordtables.components.tables.sc.partners.ScPartnersUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.partners.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.partners.ScPartnersUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.partners.partnerInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPartnersUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPartnersUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScPartnersUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-partners/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScPartnersUpdateRequest): ScPartnersUpdateResponse {

        if (req.token == null) return ScPartnersUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScPartnersUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScPartnersUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "organization" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partners",
                    column = "organization",
                    id = req.id,
                    value = req.value,
                )
            }
            "active" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partners",
                    column = "active",
                    id = req.id,
                    value = req.value,
                )
            }
            "financial_reporting_types" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partners",
                    column = "financial_reporting_types",
                    id = req.id,
                    value = req.value,
                )
            }
            "is_innovations_client" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partners",
                    column = "is_innovations_client",
                    id = req.id,
                    value = req.value,
                )
            }
            "pmc_entity_code" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partners",
                    column = "pmc_entity_code",
                    id = req.id,
                    value = req.value,
                )
            }
            "point_of_contact" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partners",
                    column = "point_of_contact",
                    id = req.id,
                    value = req.value,
                )
            }
            "types" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partners",
                    column = "types",
                    id = req.id,
                    value = req.value,
                )
            }

            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partners",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.partners",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return ScPartnersUpdateResponse(ErrorType.NoError)
    }

}