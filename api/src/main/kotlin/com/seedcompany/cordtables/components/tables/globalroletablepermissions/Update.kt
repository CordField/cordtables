package com.seedcompany.cordtables.components.tables.globalroletablepermissions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import java.sql.Time
import java.sql.Timestamp
import javax.sql.DataSource
import kotlin.reflect.full.memberProperties
import kotlin.collections.mutableListOf as mutableListOf
import java.time.Instant

data class UpdatePermissionsResponse(
    val error: ErrorType,
    val globalRolesTablePermissions: GlobalRolesTablePermissions?
)

data class UpdatablePermissionsFields(
    val tableName: String,
    val globalRole: Int,
    val tablePermissions: String,
)

data class UpdatePermissionsRequest(
    val updatedFields: UpdatablePermissionsFields,
    val email: String,
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

        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from public.users where email = ?")
                getUserIdStatement.setString(1, req.email)
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
                var updateSQL = "update public.global_roles_table_permissions set"
                for (prop in UpdatePermissionsRequest::class.memberProperties) {
                    val propValue = prop.get(req)
                    println("$propValue $(prop.name) = ?,")
                    if (propValue != null) {
                        updateSQL = "$updateSQL ${prop.name} = ?,"
                        reqValues.add(propValue)
                    }

                }
                updateSQL = "$updateSQL modified_by = ?, modified_at = ? where id = ? returning *"
                println(updateSQL)
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
                updateStatement.setTimestamp(counter+1, Timestamp(Instant.now().toEpochMilli()))
                updateStatement.setInt(counter, userId)
                updateStatement.setInt(counter+2, req.id)
                val updateStatementResult = updateStatement.executeQuery()

                if (updateStatementResult.next()) {
                    val id = updateStatementResult.getInt("id")
                    val tableName = updateStatementResult.getString("table_name")
                    val createdBy = updateStatementResult.getInt("created_by")
                    val createdAt = updateStatementResult.getString("created_at")
                    val modifiedAt = updateStatementResult.getString("modified_at")
                    val modifiedBy = updateStatementResult.getInt("modified_by")
                    val tablePermissions = updateStatementResult.getString("table_permissions")
                    val globalRole = updateStatementResult.getInt("global_role")
                    updatedPermission = GlobalRolesTablePermissions(id, tableName, createdBy, createdAt, modifiedBy, modifiedAt, tablePermissions, globalRole)
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