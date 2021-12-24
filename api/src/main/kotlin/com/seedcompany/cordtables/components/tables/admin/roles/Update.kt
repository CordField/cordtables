package com.seedcompany.cordtables.components.tables.admin.roles

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRolesUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminRolesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRolesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/roles/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminRolesUpdateRequest): AdminRolesUpdateResponse {

        if (req.token == null) return AdminRolesUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return AdminRolesUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return AdminRolesUpdateResponse(ErrorType.MissingId)


        when (req.column) {
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.roles",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.roles",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.roles",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return AdminRolesUpdateResponse(ErrorType.NoError)
    }

}
