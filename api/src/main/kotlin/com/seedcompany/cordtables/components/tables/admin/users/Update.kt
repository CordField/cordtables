package com.seedcompany.cordtables.components.tables.admin.users

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminUsersUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminUsersUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminUsersUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin-users/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminUsersUpdateRequest): AdminUsersUpdateResponse {

        if (req.token == null) return AdminUsersUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return AdminUsersUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return AdminUsersUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.users",
                    column = "person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "email" -> {
                if (req.value == null || !util.isEmailValid(req.value as String?)) return AdminUsersUpdateResponse(ErrorType.InvalidEmail)
                util.updateField(
                    token = req.token,
                    table = "admin.users",
                    column = "email",
                    id = req.id,
                    value = req.value,
                )
            }
            "password" -> {
                var password: String = req.value as String
                if (password == null || password.length < 8) return AdminUsersUpdateResponse(ErrorType.PasswordTooShort)
                if (password.length > 32) return AdminUsersUpdateResponse(ErrorType.PasswordTooLong)
                util.updateField(
                    token = req.token,
                    table = "admin.users",
                    column = "password",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.users",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.users",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return AdminUsersUpdateResponse(ErrorType.NoError)
    }

}
