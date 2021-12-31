package com.seedcompany.cordtables.components.tables.admin.roles

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.roles.AdminRolesReadRequest
import com.seedcompany.cordtables.components.tables.admin.roles.AdminRolesUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.roles.role
import com.seedcompany.cordtables.components.tables.admin.roles.roleInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRolesUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminRolesUpdateReadResponse(
    val error: ErrorType,
    val role: role? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRolesUpdateRead")
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
    @PostMapping("admin/roles/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: AdminRolesUpdateReadRequest): AdminRolesUpdateReadResponse {

        val updateResponse = update.updateHandler(
            AdminRolesUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return AdminRolesUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            AdminRolesReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return AdminRolesUpdateReadResponse(error = readResponse.error, readResponse.role)
    }
}
