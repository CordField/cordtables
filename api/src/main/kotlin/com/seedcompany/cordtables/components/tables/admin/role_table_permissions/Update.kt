package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

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
import kotlin.reflect.full.memberProperties
import kotlin.collections.mutableListOf as mutableListOf
    
data class UpdatePermissionsResponse(
        val error: ErrorType,
        val globalRolesTablePermissions: GlobalRolesTablePermissions?
)

data class UpdatePermissionsRequest(
        val table_name: String? = null,
        val role: Int? = null,
        val table_permission: String ? = null,
        val token: String,
        val id: Int
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("UpdateTablePermissions")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("globalrolestablepermissions/update")
    @ResponseBody
    fun UpdateHandler(@RequestBody req: UpdatePermissionsRequest): UpdatePermissionsResponse {

        println("req: $req")
        var updatedPermission: GlobalRolesTablePermissions? = null
        var userId = 0

        if (req.token == null) return UpdatePermissionsResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return UpdatePermissionsResponse(ErrorType.AdminOnly, null)

        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from admin.tokens where token = ?")
                getUserIdStatement.setString(1, req.token)
                val getUserIdResult = getUserIdStatement.executeQuery()
                if (getUserIdResult.next()) {
                    userId = getUserIdResult.getInt("person")
                    println("userId: $userId")
                } else {
                    throw SQLException("User not found")
                }
            } catch(e:SQLException){
                println(e.message)
                return UpdatePermissionsResponse(ErrorType.UserNotFound, null)
            }
            try {

                var reqValues: MutableList<Any> = mutableListOf()
                var updateSQL = "update admin.role_table_permissions set"
                for (prop in UpdatePermissionsRequest::class.memberProperties) {
                    val propValue = prop.get(req)

                    if (propValue !== "" && prop.name == "table_name") {
                        updateSQL = "$updateSQL ${prop.name} = ?::admin.table_name,"
                        if (propValue != null) {
                            reqValues.add(propValue)
                        }
                    }
                    if (propValue !== "" && prop.name == "role") {
                        updateSQL = "$updateSQL ${prop.name} = ?,"
                        if (propValue != null) {
                            reqValues.add(propValue)
                        }
                    }
                    if (propValue !== "" && prop.name == "table_permission") {
                        updateSQL = "$updateSQL ${prop.name} = ?::admin.table_permission,"
                        if (propValue != null) {
                            reqValues.add(propValue)
                        }
                    }

                }
                updateSQL = "$updateSQL modified_by = ? where id = ? returning *"
                val updateStatement = conn.prepareCall(
                        updateSQL
                )
                var counter = 1
                reqValues.forEach { value ->
                    when (value) {
                        is String -> updateStatement.setString(counter, value)
                        is Int -> updateStatement.setInt(counter, value)
                    }
                    counter += 1
                }

                updateStatement.setInt(counter, userId)
                updateStatement.setInt(counter+1, req.id)
                val updateStatementResult = updateStatement.executeQuery()

                if (updateStatementResult.next()) {
                    val id = updateStatementResult.getInt("id")
                    val tableName = updateStatementResult.getString("table_name")
                    val createdBy = updateStatementResult.getInt("created_by")
                    val createdAt = updateStatementResult.getString("created_at")
                    val modifiedBy = updateStatementResult.getInt("modified_by")
                    val modifiedAt = updateStatementResult.getString("modified_at")
                    val tablePermission = updateStatementResult.getString("table_permission")
                    val globalRole = updateStatementResult.getInt("role")
                    updatedPermission = GlobalRolesTablePermissions(id, modifiedAt , createdBy, createdAt, modifiedBy,  tableName , tablePermission, globalRole)
                    println("updated row's id: $id")
                }
            } catch (e:SQLException){
                println(e.message)
                return UpdatePermissionsResponse(ErrorType.SQLUpdateError, null)
            }
        }
        return UpdatePermissionsResponse(ErrorType.NoError, updatedPermission)

    }
}