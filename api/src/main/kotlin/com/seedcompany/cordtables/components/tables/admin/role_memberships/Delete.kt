package com.seedcompany.cordtables.components.tables.admin.role_memberships

import com.seedcompany.cordtables.components.tables.admin.role_memberships.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_memberships.AdminRoleMembershipsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class AdminRoleMembershipsDeleteRequest(
    val id: String,
    val token: String?,
)

data class AdminRoleMembershipsDeleteResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleMembershipsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/role-memberships/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: AdminRoleMembershipsDeleteRequest): AdminRoleMembershipsDeleteResponse {

      if (req.token == null) return AdminRoleMembershipsDeleteResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminRoleMembershipsDeleteResponse(ErrorType.AdminOnly)

        if (req.token == null) return AdminRoleMembershipsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "admin.role_memberships"))
            return AdminRoleMembershipsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from admin.role_memberships where id = ?::uuid returning id"
                )
                deleteStatement.setString(1, req.id)

                deleteStatement.setString(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return AdminRoleMembershipsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return AdminRoleMembershipsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
