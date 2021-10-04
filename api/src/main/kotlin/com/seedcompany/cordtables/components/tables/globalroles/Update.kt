package com.seedcompany.cordtables.components.tables.globalroles

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.user.GlobalRole
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

data class UpdateGlobalRoleResponse(
    val error: ErrorType,
    val data: GlobalRole?
)

data class UpdatableGlobalRoleFields(
    val name: String? ,
    val org: Int?
)

data class UpdateGlobalRoleRequest(
    val updatedFields: UpdatableGlobalRoleFields,
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
    @PostMapping("globalroles/update")
    @ResponseBody
    fun UpdateHandler(@RequestBody req: UpdateGlobalRoleRequest): UpdateGlobalRoleResponse {

        println("req: $req")
        var updatedGlobalRole: GlobalRole? = null
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
            } catch (e: SQLException) {
                println(e.message)
                return UpdateGlobalRoleResponse(ErrorType.UserNotFound, null)
            }
            try {
                var reqValues: MutableList<Any> = mutableListOf()
                var updateSql = "update public.global_roles set"
                for (prop in UpdatableGlobalRoleFields::class.memberProperties) {
                    val propValue = prop.get(req.updatedFields)
                    println("$propValue ${prop.name}")
                    if (propValue != null) {
                        updateSql = "$updateSql ${prop.name} = ?,"
                        reqValues.add(propValue)
                    }
                }
                updateSql = "$updateSql modified_by = ?, modified_at = ? where id = ? returning *"
                println(updateSql)
                val updateStatement = conn.prepareCall(
                    updateSql
                )
                var counter = 1
                reqValues.forEach { value ->
                    when (value) {
                        is Int -> updateStatement.setInt(counter, value)
                        is String -> updateStatement.setString(counter, value)
                    }
                    counter += 1
                }
//                modified_by, modified_at, id
                updateStatement.setInt(counter, userId)
                updateStatement.setTimestamp(counter+1,  Timestamp(Instant.now().toEpochMilli()))
                updateStatement.setInt(counter+2, req.id)
                val updateStatementResult = updateStatement.executeQuery()
//
                if (updateStatementResult.next()) {
                    val id = updateStatementResult.getInt("id")
                    val name = updateStatementResult.getString("name")
                    val createdBy = updateStatementResult.getInt("created_by")
                    val modifiedBy = updateStatementResult.getInt("modified_by")
                    val org = updateStatementResult.getInt("org")
                    val createdAt = updateStatementResult.getString("created_at")
                    val modifiedAt = updateStatementResult.getString("modified_at")
                    updatedGlobalRole = GlobalRole(id, createdAt, createdBy, modifiedAt, modifiedBy, name, org)
                    println("updated row's id: $id")
                }
            } catch (e: SQLException) {
                println(e.message)
                return UpdateGlobalRoleResponse(ErrorType.SQLUpdateError, null)
            }
        }
        return UpdateGlobalRoleResponse(ErrorType.NoError, updatedGlobalRole)
    }
}
