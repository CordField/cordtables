package com.seedcompany.cordtables.components.tables.admin.role_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleMembershipsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminRoleMembershipsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleMembershipsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin-role-memberships/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminRoleMembershipsUpdateRequest): AdminRoleMembershipsUpdateResponse {

        if (req.token == null) return AdminRoleMembershipsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return AdminRoleMembershipsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return AdminRoleMembershipsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "role" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_memberships",
                    column = "role",
                    id = req.id,
                    value = req.value
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_memberships",
                    column = "person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_memberships",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.role_memberships",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return AdminRoleMembershipsUpdateResponse(ErrorType.NoError)
    }

}
