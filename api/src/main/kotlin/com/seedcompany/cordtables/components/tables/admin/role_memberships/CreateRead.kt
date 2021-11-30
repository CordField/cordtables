package com.seedcompany.cordtables.components.tables.admin.role_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_memberships.*
import com.seedcompany.cordtables.components.tables.admin.role_memberships.AdminRoleMembershipsCreateRequest
import com.seedcompany.cordtables.components.tables.admin.role_memberships.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleMembershipsCreateReadRequest(
    val token: String? = null,
    val roleMembership: roleMembershipInput,
)

data class AdminRoleMembershipsCreateReadResponse(
    val error: ErrorType,
    val roleMembership: roleMembership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminRoleMembershipsCreateRead")
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
    @PostMapping("admin-role-memberships/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: AdminRoleMembershipsCreateReadRequest): AdminRoleMembershipsCreateReadResponse {

        val createResponse = create.createHandler(
            AdminRoleMembershipsCreateRequest(
                token = req.token,
                roleMembership = req.roleMembership
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return AdminRoleMembershipsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            AdminRoleMembershipsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return AdminRoleMembershipsCreateReadResponse(error = readResponse.error, roleMembership = readResponse.roleMembership)
    }
}