package com.seedcompany.cordtables.components.user

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


data class InsertableGlobalRoleFields(
    val name: String ,
    val org: Int
)

data class CreateGlobalRoleResponse(
    val error: ErrorType,
    val data: GlobalRole?
)
data class CreateGlobalRoleRequest(
    val insertedFields: InsertableGlobalRoleFields,
    val email: String,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller()
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("global_role/create")
    @ResponseBody
    fun CreateHandler(@RequestBody req: CreateGlobalRoleRequest): CreateGlobalRoleResponse {

        println("req: $req")
        var errorType = ErrorType.UnknownError
        var insertedGlobalRole: GlobalRole? = null
        var userId = 0

        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from common.users where email = ?")
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
                    "insert into admin.global_roles(name, org, created_by, modified_by) values(?,?,?, ?) returning *"
                )
                insertStatement.setString(1, req.insertedFields.name)
                insertStatement.setInt(2, req.insertedFields.org)
                insertStatement.setInt(3, userId)
                insertStatement.setInt(4, userId)


                val insertStatementResult = insertStatement.executeQuery()

                if (insertStatementResult.next()) {
                    val id = insertStatementResult.getInt("id")
                    val name = insertStatementResult.getString("name")
                    val createdBy = insertStatementResult.getInt("created_by")
                    val modifiedBy = insertStatementResult.getInt("modified_by")
                    val org = insertStatementResult.getInt("org")
                    val createdAt = insertStatementResult.getString("created_at")
                    val modifiedAt = insertStatementResult.getString("modified_at")
                    insertedGlobalRole = GlobalRole(id,createdAt,createdBy,modifiedAt,modifiedBy,name,org)
                    println("newly inserted id: $id")
                }
            }
            catch (e:SQLException ){
                println(e.message)
                errorType = ErrorType.SQLInsertError
                return CreateGlobalRoleResponse(errorType, null)
            }
        }
       return CreateGlobalRoleResponse(ErrorType.NoError,insertedGlobalRole)
    }
}
