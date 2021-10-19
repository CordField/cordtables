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
import java.sql.Timestamp
import javax.sql.DataSource
import kotlin.collections.mutableListOf as mutableListOf
import java.time.Instant

data class UpdateGlobalRoleResponse(
        val error: ErrorType,
        val data: GlobalRole?
)

data class UpdateGlobalRoleRequest(
        val columnToUpdate: String,
        val updatedColumnValue: Any,
        val token: String?,
        val id: Int
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller()
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val globalRoleUtil: GlobalRoleUtil
) {
    @PostMapping("role/update")
    @ResponseBody
    fun UpdateHandler(@RequestBody req: UpdateGlobalRoleRequest): UpdateGlobalRoleResponse {
        if (req.token == null) return UpdateGlobalRoleResponse(ErrorType.TokenNotFound, null)
        if (!util.userHasUpdatePermission(req.token, "admin.roles", req.columnToUpdate)) {
            return UpdateGlobalRoleResponse(ErrorType.DoesNotHaveUpdatePermission, null)
        }
        println("req: $req")
        var updatedGlobalRole: GlobalRole? = null
        var userId = 0


        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from admin.tokens where token= ?")
                getUserIdStatement.setString(1, req.token)
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
                var updateSql = "update admin.roles set"
                if (req.updatedColumnValue != null && req.columnToUpdate !in globalRoleUtil.nonMutableColumns) {
                    updateSql = "$updateSql ${req.columnToUpdate} = ?,"
                    reqValues.add(req.updatedColumnValue)
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
                        is Double -> updateStatement.setDouble(counter, value)
                        else -> updateStatement.setObject(counter, value, java.sql.Types.OTHER)
                    }
                    counter += 1
                }
//                modified_by, modified_at, id
                updateStatement.setInt(counter, userId)
                updateStatement.setTimestamp(counter + 1, Timestamp(Instant.now().toEpochMilli()))
                updateStatement.setInt(counter + 2, req.id)
                val updateStatementResult = updateStatement.executeQuery()
//
                if (updateStatementResult.next()) {
                    var id: Int? = updateStatementResult.getInt("id")
                    if(updateStatementResult.wasNull()) id = null
                    var name: String? = updateStatementResult.getString("name")
                    if(updateStatementResult.wasNull()) name = null
                    var created_by: Int? = updateStatementResult.getInt("created_by")
                    if(updateStatementResult.wasNull()) created_by = null
                    var modified_by: Int? = updateStatementResult.getInt("modified_by")
                    if(updateStatementResult.wasNull()) modified_by = null
                    var owning_group: Int? = updateStatementResult.getInt("owning_group")
                    if(updateStatementResult.wasNull()) owning_group = null
                    var owning_person: Int? = updateStatementResult.getInt("owning_person")
                    if(updateStatementResult.wasNull()) owning_person = null
                    var chat: Int? = updateStatementResult.getInt("chat")
                    if(updateStatementResult.wasNull()) chat = null
                    var created_at: String? = updateStatementResult.getString("created_at")
                    if(updateStatementResult.wasNull()) created_at = null
                    var modified_at: String? = updateStatementResult.getString("modified_at")
                    if(updateStatementResult.wasNull()) modified_at = null
                    updatedGlobalRole = GlobalRole(id =id, chat=chat,owning_group=owning_group,owning_person=owning_person, created_at =created_at,created_by =  created_by, modified_at =modified_at,modified_by= modified_by,name=name)
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
