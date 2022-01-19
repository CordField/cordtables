package com.seedcompany.cordtables.components.tables.admin.roles

import com.seedcompany.cordtables.components.tables.admin.roles.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.roles.AdminRolesDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class AdminRolesDeleteRequest(
    val id: String,
    val token: String?,
)

data class AdminRolesDeleteResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRolesDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/roles/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: AdminRolesDeleteRequest): AdminRolesDeleteResponse {

      if (req.token == null) return AdminRolesDeleteResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminRolesDeleteResponse(ErrorType.AdminOnly)

        if (req.token == null) return AdminRolesDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "admin.roles"))
            return AdminRolesDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from admin.roles where id = ? returning id"
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

                return AdminRolesDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return AdminRolesDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
