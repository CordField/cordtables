package com.seedcompany.cordtables.components.tables.admin.role_column_grants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.AdminRoleColumnGrantsReadRequest
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.AdminRoleColumnGrantsUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.roleColumnGrant
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.roleColumnGrantInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleColumnGrantsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminRoleColumnGrantsUpdateReadResponse(
    val error: ErrorType,
    val roleColumnGrant: roleColumnGrant? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleColumnGrantsUpdateRead")
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
    @PostMapping("admin-role-column-grants/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: AdminRoleColumnGrantsUpdateReadRequest): AdminRoleColumnGrantsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            AdminRoleColumnGrantsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return AdminRoleColumnGrantsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            AdminRoleColumnGrantsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return AdminRoleColumnGrantsUpdateReadResponse(error = readResponse.error, readResponse.roleColumnGrant)
    }
}
