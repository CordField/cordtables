package com.seedcompany.cordtables.components.tables.admin.users

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.roles.CreateGlobalRoleResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource
import kotlin.reflect.full.memberProperties


data class AdminUserCreateResponse(
  val error: ErrorType,
  val data: AdminUser?
)

data class AdminUserCreateRequest(
  val insertedFields: AdminUser,
  val token: String?,
)

data class AdminUser(
  val id: Int?,
  val person: Int?,
  val email: String?,
  val password: String?,
  val chat: Int?,
  val created_at: String?,
  val created_by: Int?,
  val modified_at: String?,
  val modified_by: Int?,
  val owning_person: Int?,
  val owning_group: Int?,
  val peer: Int?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminUserCreate")
class Create(
  @Autowired
    val util: Utility,
    val adminUserUtil: AdminUserUtil,

  @Autowired
    val ds: DataSource,
) {
    @PostMapping("table/admin-users/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminUserCreateRequest): AdminUserCreateResponse {

        if (req.token == null) return AdminUserCreateResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return AdminUserCreateResponse(ErrorType.AdminOnly, null)

        if (!util.userHasCreatePermission(req.token, "admin.users"))
            return AdminUserCreateResponse(ErrorType.DoesNotHaveCreatePermission, null)

        println("req: $req")
        var insertedAdminUser: AdminUser? = null
        var userId = 0
        val reqValues: MutableList<Any> = mutableListOf()
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
                return AdminUserCreateResponse(ErrorType.UserNotFound, null)
            }
            try {
                var insertStatementKeys = "insert into admin.users("
                var insertStatementValues = " values("
                for (prop in AdminUser::class.memberProperties) {
                    var propValue = prop.get(req.insertedFields)
                    println("$propValue ${prop.name}")
                    if (propValue != null && prop.name !in adminUserUtil.nonMutableColumns) {
                        insertStatementKeys = "$insertStatementKeys ${prop.name},"
                        insertStatementValues = "$insertStatementValues ?,"
                        if(prop.name == "password") propValue = util.encoder.encode(propValue as String)
                        reqValues.add(propValue as Any)
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
                    if (insertStatementResult.wasNull()) id = null
                    var person: Int? = insertStatementResult.getInt("person")
                    if (insertStatementResult.wasNull()) person = null
                    var email: String? = insertStatementResult.getString("email")
                    if (insertStatementResult.wasNull()) email = null
                    var chat: Int? = insertStatementResult.getInt("chat")
                    if (insertStatementResult.wasNull()) chat = null
                    var createdAt: String? = insertStatementResult.getString("created_at")
                    if (insertStatementResult.wasNull()) createdAt = null
                    var createdBy: Int? = insertStatementResult.getInt("created_by")
                    if (insertStatementResult.wasNull()) createdBy = null
                    var modifiedAt: String? = insertStatementResult.getString("modified_at")
                    if (insertStatementResult.wasNull()) modifiedAt = null
                    var modifiedBy: Int? = insertStatementResult.getInt("modified_by")
                    if (insertStatementResult.wasNull()) modifiedBy = null
                    var owningPerson: Int? = insertStatementResult.getInt("owning_person")
                    if (insertStatementResult.wasNull()) owningPerson = null
                    var owningGroup: Int? = insertStatementResult.getInt("owning_group")
                    if (insertStatementResult.wasNull()) owningGroup = null
                    var peer: Int? = insertStatementResult.getInt("peer")
                    if (insertStatementResult.wasNull()) peer = null

                    insertedAdminUser = AdminUser(
                        id= id,
                        person = person,
                        email = email,
                        chat = chat,
                        password = null,
                        created_at = createdAt,
                        created_by = createdBy,
                        modified_at = modifiedAt,
                        modified_by = modifiedBy,
                        owning_person= owningPerson,
                        owning_group =  owningGroup,
                        peer = peer,
                    )
                    println("newly inserted id: $id")
                }
            } catch (e: SQLException) {
                println(e.message)
                return AdminUserCreateResponse(ErrorType.SQLInsertError, null)
            }
        }
        return AdminUserCreateResponse(ErrorType.NoError, insertedAdminUser)
    }
}
