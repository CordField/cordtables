package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.role_memberships.GlobalRoleMembershipsCreateReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class InsertableGRTPFields(
    val tableName: String,
    val globalRole: Int,
    val tablePermission: String,
)

data class CreateGRTPermissionsResponse(
    val error: ErrorType,
    val data: GlobalRolesTablePermissions?
)
data class CreateGRTPermissionsRequest(
    val token: String,
        val insertedFields: InsertableGRTPFields,
        val token: String,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CreateTablePermissions")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("globalrolestablepermissions/create")
    @ResponseBody
    fun CreateHandler(@RequestBody req: CreateGRTPermissionsRequest): CreateGRTPermissionsResponse {

        if (!util.isAdmin(req.token)) return CreateGRTPermissionsResponse(ErrorType.AdminOnly, null)
        println("req: $req")
        var errorType = ErrorType.UnknownError
        var newGRTPermission: GlobalRolesTablePermissions? = null
        var userId = 0

        if (req.token == null) return CreateGRTPermissionsResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return CreateGRTPermissionsResponse(ErrorType.AdminOnly, null)

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
                return CreateGRTPermissionsResponse(errorType, null)
            }
            try {
                val insertStatement = conn.prepareCall(
                    "insert into admin.role_table_permissions(table_name, table_permission, role, created_by, modified_by, owning_person, owning_group) values(?::admin.table_name,?::admin.table_permission,?, ?, ?, ?, ?) returning *"
                )
                insertStatement.setString(1, req.insertedFields.tableName)
                insertStatement.setString(2, req.insertedFields.tablePermission)
                insertStatement.setInt(3, req.insertedFields.globalRole)
                insertStatement.setInt(4, userId)
                insertStatement.setInt(5, userId)
                insertStatement.setInt(6, userId)
                insertStatement.setInt(7, 1)

                val insertStatementResult = insertStatement.executeQuery()

                if (insertStatementResult.next()) {
                    val id = insertStatementResult.getInt("id")
                    val createdAt = insertStatementResult.getString("created_at")
                    val createdBy = insertStatementResult.getInt("created_by")
                    val modifiedAt = insertStatementResult.getString("modified_at")
                    val modifiedBy = insertStatementResult.getInt("modified_by")
                    val tableName = insertStatementResult.getString("table_name")
                    val tablePermission = insertStatementResult.getString("table_permission")
                    val globalRole = insertStatementResult.getInt("role")
                    newGRTPermission = GlobalRolesTablePermissions(id, createdAt, createdBy, modifiedAt, modifiedBy, tableName, tablePermission, globalRole)
                    println("newly inserted id: $id")
                }
            }
            catch (e:SQLException ){
                println(e.message)
                errorType = ErrorType.SQLInsertError
                return CreateGRTPermissionsResponse(errorType, null)
            }
        }
        return CreateGRTPermissionsResponse(ErrorType.NoError,newGRTPermission)
    }
}