package com.seedcompany.cordtables.components.tables.admin.users

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.users.AdminUsersReadRequest
import com.seedcompany.cordtables.components.tables.admin.users.AdminUsersUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.users.user
import com.seedcompany.cordtables.components.tables.admin.users.userInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminUsersUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminUsersUpdateReadResponse(
    val error: ErrorType,
    val user: user? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminUsersUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("admin/users/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: AdminUsersUpdateReadRequest): AdminUsersUpdateReadResponse {

        val updateResponse = update.updateHandler(
            AdminUsersUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return AdminUsersUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            AdminUsersReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return AdminUsersUpdateReadResponse(error = readResponse.error, readResponse.user)
    }
}
