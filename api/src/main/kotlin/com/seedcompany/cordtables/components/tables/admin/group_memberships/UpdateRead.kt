package com.seedcompany.cordtables.components.tables.admin.group_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.group_memberships.AdminGroupMembershipsReadRequest
import com.seedcompany.cordtables.components.tables.admin.group_memberships.AdminGroupMembershipsUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.group_memberships.groupMembership
import com.seedcompany.cordtables.components.tables.admin.group_memberships.groupMembershipInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupMembershipsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminGroupMembershipsUpdateReadResponse(
    val error: ErrorType,
    val groupMembership: groupMembership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupMembershipsUpdateRead")
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
    @PostMapping("admin-group-memberships/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: AdminGroupMembershipsUpdateReadRequest): AdminGroupMembershipsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            AdminGroupMembershipsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return AdminGroupMembershipsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            AdminGroupMembershipsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return AdminGroupMembershipsUpdateReadResponse(error = readResponse.error, readResponse.groupMembership)
    }
}
