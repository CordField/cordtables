package com.seedcompany.cordtables.components.tables.admin.group_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.group_memberships.*
import com.seedcompany.cordtables.components.tables.admin.group_memberships.AdminGroupMembershipsCreateRequest
import com.seedcompany.cordtables.components.tables.admin.group_memberships.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupMembershipsCreateReadRequest(
    val token: String? = null,
    val groupMembership: groupMembershipInput,
)

data class AdminGroupMembershipsCreateReadResponse(
    val error: ErrorType,
    val groupMembership: groupMembership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupMembershipsCreateRead")
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
    @PostMapping("admin/group-memberships/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: AdminGroupMembershipsCreateReadRequest): AdminGroupMembershipsCreateReadResponse {

      if (req.token == null) return AdminGroupMembershipsCreateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminGroupMembershipsCreateReadResponse(ErrorType.AdminOnly)

        val createResponse = create.createHandler(
            AdminGroupMembershipsCreateRequest(
                token = req.token,
                groupMembership = req.groupMembership
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return AdminGroupMembershipsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            AdminGroupMembershipsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return AdminGroupMembershipsCreateReadResponse(error = readResponse.error, groupMembership = readResponse.groupMembership)
    }
}
