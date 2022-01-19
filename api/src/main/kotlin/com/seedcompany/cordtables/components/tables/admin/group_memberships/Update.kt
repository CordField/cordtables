package com.seedcompany.cordtables.components.tables.admin.group_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupMembershipsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminGroupMembershipsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupMembershipsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/group-memberships/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminGroupMembershipsUpdateRequest): AdminGroupMembershipsUpdateResponse {

      if (req.token == null) return AdminGroupMembershipsUpdateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminGroupMembershipsUpdateResponse(ErrorType.AdminOnly)
        if (req.column == null) return AdminGroupMembershipsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return AdminGroupMembershipsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "group_id" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.group_memberships",
                    column = "group_id",
                    id = req.id,
                    value = req.value
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.group_memberships",
                    column = "person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.group_memberships",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.group_memberships",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return AdminGroupMembershipsUpdateResponse(ErrorType.NoError)
    }

}
