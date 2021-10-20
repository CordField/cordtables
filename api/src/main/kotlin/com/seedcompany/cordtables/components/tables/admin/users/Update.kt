package com.seedcompany.cordtables.components.tables.admin.users

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
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

data class AdminUserUpdateResponse(
    val error: ErrorType,
    val data: AdminUser?
)

data class AdminUserUpdateRequest(
    val columnToUpdate: String,
    val updatedColumnValue: Any?,
    val token: String?,
    val id: Int
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminUserUpdate")
class Update(
        @Autowired
        val util: Utility,
        val adminUserUtil: AdminUserUtil,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("table/admin-users/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminUserUpdateRequest): AdminUserUpdateResponse {
        if (req.token == null) return AdminUserUpdateResponse(ErrorType.TokenNotFound, null)
        if (!util.userHasUpdatePermission(req.token, "admin.users", req.columnToUpdate)) {
            return AdminUserUpdateResponse(ErrorType.DoesNotHaveUpdatePermission, null)
        }
        println("req: $req")
        var updatedAdminUser: AdminUser? = null
        var userId = 0

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
            } catch (e: SQLException) {
                println(e.message)
                return AdminUserUpdateResponse(ErrorType.UserNotFound, null)
            }
            try {
                val reqValues: MutableList<Any> = mutableListOf()
                var updateSql = "update admin.users set"

                if (req.updatedColumnValue != null && req.columnToUpdate !in adminUserUtil.nonMutableColumns) {
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
                    if (updateStatementResult.wasNull()) id = null

                    var person: Int? = updateStatementResult.getInt("person")
                    if (updateStatementResult.wasNull()) person = null

                    var email: String? = updateStatementResult.getString("iso")
                    if (updateStatementResult.wasNull()) email = null

                    var chat: Int? = updateStatementResult.getInt("chat")
                    if (updateStatementResult.wasNull()) chat = null

                    var createdAt: String? = updateStatementResult.getString("created_at")
                    if (updateStatementResult.wasNull()) createdAt = null

                    var createdBy: Int? = updateStatementResult.getInt("created_by")
                    if (updateStatementResult.wasNull()) createdBy = null

                    var modifiedAt: String? = updateStatementResult.getString("modified_at")
                    if (updateStatementResult.wasNull()) modifiedAt = null

                    var modifiedBy: Int? = updateStatementResult.getInt("modified_by")
                    if (updateStatementResult.wasNull()) modifiedBy = null

                    var owningPerson: Int? = updateStatementResult.getInt("owning_person")
                    if (updateStatementResult.wasNull()) owningPerson = null

                    var owningGroup: Int? = updateStatementResult.getInt("owning_group")
                    if (updateStatementResult.wasNull()) owningGroup = null

                    var peer: Int? = updateStatementResult.getInt("peer")
                    if (updateStatementResult.wasNull()) peer = null

                    updatedAdminUser =  AdminUser(
                        id= id,
                        person = person,
                        email = email,
                        password = null,
                        chat = chat,
                        created_at = createdAt,
                        created_by = createdBy,
                        modified_at = modifiedAt,
                        modified_by = modifiedBy,
                        owning_person= owningPerson,
                        owning_group =  owningGroup,
                        peer = peer,
                    )
                    println("updated row's id: $id")
                }
            } catch (e: SQLException) {
                println(e.message)
                return AdminUserUpdateResponse(ErrorType.SQLUpdateError, null)
            }
        }
        return AdminUserUpdateResponse(ErrorType.NoError, updatedAdminUser)
    }
}
