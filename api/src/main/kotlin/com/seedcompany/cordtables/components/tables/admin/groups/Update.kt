package com.seedcompany.cordtables.components.tables.admin.groups

import com.seedcompany.cordtables.components.tables.admin.groups.AdminGroupsUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.groups.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.admin.groups.AdminGroupsUpdateResponse
import com.seedcompany.cordtables.components.tables.admin.groups.groupInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminGroupsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminGroupsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin-groups/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminGroupsUpdateRequest): AdminGroupsUpdateResponse {

        if (req.token == null) return AdminGroupsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return AdminGroupsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return AdminGroupsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.groups",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "parent_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.groups",
                    column = "parent_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.groups",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.groups",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return AdminGroupsUpdateResponse(ErrorType.NoError)
    }

}