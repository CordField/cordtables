package com.seedcompany.cordspringstencil.components.tables.globalroles

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import com.seedcompany.cordspringstencil.components.user.GlobalRolesTablePermissions
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

data class UpdatePermissionsRequest(
    val tableName: String,
    val globalRole: Int,
    val tablePermissions: String,
    val email: String,
    val id: Int
)

@CrossOrigin(origins = ["http://localhost:3333"])
@Controller()
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("globalrolestablepermissions/updatte")
    @ResponseBody
    fun UpdateHandler(@RequestBody req: UpdatePermissionsRequest): UpdatePermissionsResponse {

        println("req: $req")
        var updatedGRTPermissions: GlobalRolesTablePermissions? = null
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

                }

        }
        catch (e:SQLException){
            println(e.message)
            errorType = ErrorType.SQLInsertError
            return CreateGRTPermissionsResponse(errorType, null)

        }
        }
        return CreateGRTPermissionsResponse(ErrorType.NoError, newGRTPermissions)

    }
}