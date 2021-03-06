package com.seedcompany.cordtables.components.tables.admin.role_column_grants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class AdminRoleColumnGrantsDeleteRequest(
    val id: String,
    val token: String?,
)

data class AdminRoleColumnGrantsDeleteResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleColumnGrantsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/role-column-grants/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: AdminRoleColumnGrantsDeleteRequest): AdminRoleColumnGrantsDeleteResponse {

      if (req.token == null) return AdminRoleColumnGrantsDeleteResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminRoleColumnGrantsDeleteResponse(ErrorType.AdminOnly)

        if (req.token == null) return AdminRoleColumnGrantsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "admin.role_column_grants"))
            return AdminRoleColumnGrantsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from admin.role_column_grants where id = ? returning id"
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

                return AdminRoleColumnGrantsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return AdminRoleColumnGrantsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
