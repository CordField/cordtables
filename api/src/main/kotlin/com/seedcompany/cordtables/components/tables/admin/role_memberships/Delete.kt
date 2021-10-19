package com.seedcompany.cordtables.components.tables.globalrolememberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class GlobalRoleMembershipsDeleteRequest(
        val token: String? = null,
        val id: Int? = null,
)

data class GlobalRoleMembershipsDeleteResponse(
        val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GlobalRoleMembershipsDelete")
class Delete(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {

    @PostMapping("role-memberships/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: GlobalRoleMembershipsDeleteRequest): GlobalRoleMembershipsDeleteResponse {

        if (req.token == null) return GlobalRoleMembershipsDeleteResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GlobalRoleMembershipsDeleteResponse(ErrorType.AdminOnly)

        if (req.id == null) return GlobalRoleMembershipsDeleteResponse(ErrorType.MissingId)

        this.ds.connection.use { conn ->

            //language=SQL
            val deleteStatement = conn.prepareStatement(
                    """
                delete from admin.role_memberships where id = ?;
            """.trimIndent()
            )

            deleteStatement.setInt(1, req.id)

            deleteStatement.execute()
        }

        return GlobalRoleMembershipsDeleteResponse(ErrorType.NoError)
    }

}