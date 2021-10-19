package com.seedcompany.cordtables.components.user

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.globalroles.GlobalRoleUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource
import kotlin.reflect.full.memberProperties


data class InsertableGlobalRoleFields(
    val name: String ,
    val org: Int
)

data class CreateGlobalRoleResponse(
    val error: ErrorType,
    val data: GlobalRole?
)
data class CreateGlobalRoleRequest(
    val insertedFields: GlobalRole,
    val token: String,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GlobalRoleCreate")
class Create(
    @Autowired
    val util: Utility,
    @Autowired
    val globalRoleUtil: GlobalRoleUtil,
    @Autowired
    val ds: DataSource,
) {
    @PostMapping("role/create")
    @ResponseBody
    fun CreateHandler(@RequestBody req: CreateGlobalRoleRequest): CreateGlobalRoleResponse {

        println("req: $req")
        var errorType = ErrorType.UnknownError
        var insertedGlobalRole: GlobalRole? = null
        var userId = 0
        val reqValues: MutableList<Any> = mutableListOf()

        if (req.token == null) return CreateGlobalRoleResponse(ErrorType.TokenNotFound, null)
        if (!util.userHasCreatePermission(req.token, "admin.roles"))
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
                var insertStatementKeys = "insert into admin.roles("
                var insertStatementValues = " values("
                for (prop in GlobalRole::class.memberProperties) {
                    val propValue = prop.get(req.insertedFields)
                    println("$propValue ${prop.name}")
                    if (propValue != null && prop.name !in globalRoleUtil.nonMutableColumns) {
                        insertStatementKeys = "$insertStatementKeys ${prop.name},"
                        insertStatementValues = "$insertStatementValues ?,"
                        reqValues.add(propValue)
                    }
                }
                insertStatementKeys = "$insertStatementKeys modified_by,created_by)"
                insertStatementValues = "$insertStatementValues ?,?) returning *;"
                val insertStatementSQL = "$insertStatementKeys $insertStatementValues"
                println(insertStatementSQL)

                val insertStatement = conn.prepareCall(
                        insertStatementSQL
                )

                var counter = 1
                reqValues.forEach { value ->
                    when (value) {
                        is Int -> insertStatement.setInt(counter, value)
                        is String -> insertStatement.setString(counter, value)
                        is Double -> insertStatement.setDouble(counter, value)
                        else -> insertStatement.setObject(counter, value, java.sql.Types.OTHER)
                    }
                    counter += 1
                }
                insertStatement.setInt(counter, userId)
                insertStatement.setInt(counter + 1, userId)

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
                    var owning_group: Int? = insertStatementResult.getInt("owning_group")
                    if(insertStatementResult.wasNull()) owning_group = null
                    var owning_person: Int? = insertStatementResult.getInt("owning_person")
                    if(insertStatementResult.wasNull()) owning_person = null
                    var chat: Int? = insertStatementResult.getInt("chat")
                    if(insertStatementResult.wasNull()) chat = null
                    var created_at: String? = insertStatementResult.getString("created_at")
                    if(insertStatementResult.wasNull()) created_at = null
                    var modified_at: String? = insertStatementResult.getString("modified_at")
                    if(insertStatementResult.wasNull()) modified_at = null
                    insertedGlobalRole = GlobalRole(id =id, created_at =created_at,created_by =  created_by, modified_at =modified_at,modified_by= modified_by,name=name,owning_group= owning_group, owning_person = owning_person, chat=chat)
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
