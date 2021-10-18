package com.seedcompany.cordtables.components.user

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.languageex.CreateLanguageExResponse
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
    val token: String,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GlobalRoleCreate")
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

        if (req.token == null) return CreateGlobalRoleResponse(ErrorType.TokenNotFound, null)
        if (!util.userHasCreatePermission(req.token, "admin.languages_ex"))
            return CreateGlobalRoleResponse(ErrorType.DoesNotHaveCreatePermission, null)

        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from admin.tokens where token = ?")
                getUserIdStatement.setString(1, req.token)
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
                    var id: Int? = insertStatementResult.getInt("id")
                    if(insertStatementResult.wasNull()) id = null
                    var name: String? = insertStatementResult.getString("name")
                    if(insertStatementResult.wasNull()) name = null
                    var created_by: Int? = insertStatementResult.getInt("created_by")
                    if(insertStatementResult.wasNull()) created_by = null
                    var modified_by: Int? = insertStatementResult.getInt("modified_by")
                    if(insertStatementResult.wasNull()) modified_by = null
                    var org: Int? = insertStatementResult.getInt("org")
                    if(insertStatementResult.wasNull()) org = null
                    var created_at: String? = insertStatementResult.getString("created_at")
                    if(insertStatementResult.wasNull()) created_at = null
                    var modified_at: String? = insertStatementResult.getString("modified_at")
                    if(insertStatementResult.wasNull()) modified_at = null
                    insertedGlobalRole = GlobalRole(id =id, created_at =created_at,created_by =  created_by, modified_at =modified_at,modified_by= modified_by,name=name,org= org)
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
