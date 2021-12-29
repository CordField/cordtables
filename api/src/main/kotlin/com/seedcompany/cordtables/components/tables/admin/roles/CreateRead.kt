package com.seedcompany.cordtables.components.tables.admin.roles

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.roles.*
import com.seedcompany.cordtables.components.tables.admin.roles.AdminRolesCreateRequest
import com.seedcompany.cordtables.components.tables.admin.roles.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRolesCreateReadRequest(
    val token: String? = null,
    val role: roleInput,
)

data class AdminRolesCreateReadResponse(
    val error: ErrorType,
    val role: role? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRolesCreateRead")
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
    @PostMapping("admin/roles/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: AdminRolesCreateReadRequest): AdminRolesCreateReadResponse {

        val createResponse = create.createHandler(
            AdminRolesCreateRequest(
                token = req.token,
                role = req.role
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return AdminRolesCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            AdminRolesReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return AdminRolesCreateReadResponse(error = readResponse.error, role = readResponse.role)
    }
}
