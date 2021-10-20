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
import javax.sql.DataSource


data class InsertableGRTPFields(
    val tableName: String,
    val globalRole: Int,
    val tablePermissions: String,
)

data class CreateGRTPermissionsResponse(
    val error: ErrorType,
    val data: GlobalRolesTablePermissions?
)
data class CreateGRTPermissionsRequest(
    val insertedFields: InsertableGRTPFields,
    val email: String
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

        println("req: $req")
        var errorType = ErrorType.UnknownError
        var newGRTPermission: GlobalRolesTablePermissions? = null
        var userId = 0

        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from admin.users where email = ?")
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
                return CreateGRTPermissionsResponse(errorType, null)
            }
            try {
                val insertStatement = conn.prepareCall(
                    "insert into public.roles_table_permissions(table_name, table_permissions, role, created_by, modified_by) values(?,?,?,?, ?) returning *"
                )
                insertStatement.setString(1, req.insertedFields.tableName)
                insertStatement.setString(2, req.insertedFields.tablePermissions)
                insertStatement.setInt(3, req.insertedFields.globalRole)
                insertStatement.setInt(4, userId)
                insertStatement.setInt(5, userId)

                val insertStatementResult = insertStatement.executeQuery()

                if (insertStatementResult.next()) {
                    val id = insertStatementResult.getInt("id")
                    val createdAt = insertStatementResult.getString("created_at")
                    val createdBy = insertStatementResult.getInt("created_by")
                    val modifiedAt = insertStatementResult.getString("modified_at")
                    val modifiedBy = insertStatementResult.getInt("modified_by")
                    val tableName = insertStatementResult.getString("table_name")
                    val tablePermissions = insertStatementResult.getString("table_permissions")
                    val globalRole = insertStatementResult.getInt("role")
                    newGRTPermission = GlobalRolesTablePermissions(id, createdAt, createdBy, modifiedAt, modifiedBy, tableName, tablePermissions, globalRole)
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