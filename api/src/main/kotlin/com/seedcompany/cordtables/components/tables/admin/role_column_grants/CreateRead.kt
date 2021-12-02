package com.seedcompany.cordtables.components.tables.admin.role_column_grants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.*
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.AdminRoleColumnGrantsCreateRequest
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleColumnGrantsCreateReadRequest(
    val token: String? = null,
    val roleColumnGrant: roleColumnGrantInput,
)

data class AdminRoleColumnGrantsCreateReadResponse(
    val error: ErrorType,
    val roleColumnGrant: roleColumnGrant? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleColumnGrantsCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("admin-role-column-grants/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: AdminRoleColumnGrantsCreateReadRequest): AdminRoleColumnGrantsCreateReadResponse {

        val createResponse = create.createHandler(
            AdminRoleColumnGrantsCreateRequest(
                token = req.token,
                roleColumnGrant = req.roleColumnGrant
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return AdminRoleColumnGrantsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            AdminRoleColumnGrantsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return AdminRoleColumnGrantsCreateReadResponse(error = readResponse.error, roleColumnGrant = readResponse.roleColumnGrant)
    }
}