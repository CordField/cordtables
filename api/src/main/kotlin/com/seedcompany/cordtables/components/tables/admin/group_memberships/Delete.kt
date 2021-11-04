package com.seedcompany.cordtables.components.tables.admin.group_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class GroupMembershipDeleteRequest(
    val token: String? = null,
    val id: Int? = null,
)

data class GroupMembershipDeleteResponse(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GroupMembershipsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("groupmemberships/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: GroupMembershipDeleteRequest): GroupMembershipDeleteResponse {

        if (req.token == null) return GroupMembershipDeleteResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GroupMembershipDeleteResponse(ErrorType.AdminOnly)

        if (req.id == null) return GroupMembershipDeleteResponse(ErrorType.MissingId)

        this.ds.connection.use { conn ->

            //language=SQL
            val deleteStatement = conn.prepareStatement(
                """
                delete from admin.group_memberships where id = ?;
            """.trimIndent()
            )

            deleteStatement.setInt(1, req.id)

            deleteStatement.execute()
        }

        return GroupMembershipDeleteResponse(ErrorType.NoError)
    }

}