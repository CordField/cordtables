package com.seedcompany.cordtables.components.tables.sc.organizations

import com.seedcompany.cordtables.components.tables.sc.organizations.ScOrganizationsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.organizations.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.organizations.ScOrganizationsUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.organizations.organizationInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScOrganizationsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScOrganizationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScOrganizationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/organizations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScOrganizationsUpdateRequest): ScOrganizationsUpdateResponse {

        if (req.token == null) return ScOrganizationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScOrganizationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScOrganizationsUpdateResponse(ErrorType.MissingId)


        when (req.column) {
            "neo4j_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.organizations",
                    column = "neo4j_id",
                    id = req.id,
                    value = req.value,
                )
            }
            "address" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.organizations",
                    column = "address",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.organizations",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.organizations",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScOrganizationsUpdateResponse(ErrorType.NoError)
    }

}
