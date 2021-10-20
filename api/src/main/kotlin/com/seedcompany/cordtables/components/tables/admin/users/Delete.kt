package com.seedcompany.cordtables.components.tables.admin.users

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

data class AdminUserDeleteResponse(
    val error: ErrorType,
    val id: Int?
)
data class AdminUserDeleteRequest(
    val id: Int,
    val token: String?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminUserDelete")
class Delete(
    @Autowired
    val util: Utility,
    @Autowired
    val ds: DataSource,
) {
    @PostMapping("table/admin-users/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: AdminUserDeleteRequest): AdminUserDeleteResponse {

        if (req.token == null) return AdminUserDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.languages_ex"))
            return AdminUserDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from admin.users where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)

                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                  deletedId  = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)
                return AdminUserDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return AdminUserDeleteResponse(ErrorType.NoError, deletedId)
    }
}