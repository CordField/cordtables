package com.seedcompany.cordtables.components.tables.admin.role_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_memberships.AdminRoleMembershipsReadRequest
import com.seedcompany.cordtables.components.tables.admin.role_memberships.AdminRoleMembershipsUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.role_memberships.roleMembership
import com.seedcompany.cordtables.components.tables.admin.role_memberships.roleMembershipInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminRoleMembershipsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminRoleMembershipsUpdateReadResponse(
    val error: ErrorType,
    val roleMembership: roleMembership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleMembershipsUpdateRead")
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
    @PostMapping("admin/role-memberships/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: AdminRoleMembershipsUpdateReadRequest): AdminRoleMembershipsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            AdminRoleMembershipsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return AdminRoleMembershipsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            AdminRoleMembershipsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return AdminRoleMembershipsUpdateReadResponse(error = readResponse.error, readResponse.roleMembership)
    }
}
