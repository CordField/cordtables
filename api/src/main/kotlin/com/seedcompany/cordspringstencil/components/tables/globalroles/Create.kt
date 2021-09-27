package com.seedcompany.cordspringstencil.components.user

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class CreateGlobalRoleResponse(
    val error: ErrorType,
    val globalRole: GlobalRole?
)
data class CreateGlobalRoleRequest(
    val name: String,
    val org: String,
    val email: String,
)


@CrossOrigin(origins = ["http://localhost:3333"])
@Controller()
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("globalroles/create")
    @ResponseBody
    fun CreateHandler(@RequestBody req: CreateGlobalRoleRequest): CreateGlobalRoleResponse {

        println("req: $req")
        var errorType = ErrorType.UnknownError
        var insertedGlobalRole: GlobalRole? = null
        var userId = 0

        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from public.users where email = ?")
                getUserIdStatement.setString(1, req.email)
                val getUserIdResult = getUserIdStatement.executeQuery()
                if (getUserIdResult.next()) {
                    userId = getUserIdResult.getInt("person")
                    println("userId: $userId")
                }
                else {
                    throw SQLException("User not found")
                }
            }
            catch(e:SQLException){
                println(e.message)
                errorType = ErrorType.UserNotFound
                return CreateGlobalRoleResponse(errorType, null)
            }
            try {
                val insertStatement = conn.prepareCall(
                    "insert into public.global_roles(name, org, created_by, modified_by) values(?,?,?, ?) returning *"
                )
                insertStatement.setString(1, req.name)
                insertStatement.setInt(2, req.org!!.toInt())
                insertStatement.setInt(3, userId)
                insertStatement.setInt(4, userId)


                val insertStatementResult = insertStatement.executeQuery()

                if (insertStatementResult.next()) {
                    val id = insertStatement.getInt("id")
                    val name = insertStatement.getString("name")
                    val createdBy = insertStatement.getInt("created_by")
                    val modifiedBy = insertStatement.getInt("modified_by")
                    val org = insertStatement.getInt("org")
                    val createdAt = insertStatement.getString("created_at")
                    val modifiedAt = insertStatement.getString("modified_at")
                    insertedGlobalRole = GlobalRole(id,createdAt,createdBy,modifiedAt,modifiedBy,name,org)
                    println("newly inserted id: $id")
                }
            }
            catch (e:SQLException){
                println(e.message)
                errorType = ErrorType.SQLInsertError
                return CreateGlobalRoleResponse(errorType, null)
            }
        }
       return CreateGlobalRoleResponse(ErrorType.NoError,insertedGlobalRole)
    }
}
