package com.seedcompany.cordtables.components.tables.admin.role_column_grants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class GlobalRoleColumnGrantsUpdate(
        val id: Int,
        val access_level: String,
        val column_name: String? = null,
        val created_by: Int,
        val role: Int,
        val table_name: String? = null,
        val token: String,
)

data class UpdateGlobalRoleColumnGrantsResponse(
        val error: ErrorType,
        val response: GlobalRoleColumnGrantsUpdate?
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@RestController("globalRoleColumnGrantsUpdateController")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("table/role-column-grants-update")
    @ResponseBody
    fun UpdateHandler(@RequestBody req: GlobalRoleColumnGrantsUpdate): UpdateGlobalRoleColumnGrantsResponse {

        println("req: $req")
        var errorType = ErrorType.UnknownError
        var insertedGlobalRole: GlobalRoleColumnGrantsUpdate? = null
        var userId = 0

        if (req.token == null) return UpdateGlobalRoleColumnGrantsResponse(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return UpdateGlobalRoleColumnGrantsResponse(ErrorType.AdminOnly, null)

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
                errorType = ErrorType.UserTokenNotFound
                return UpdateGlobalRoleColumnGrantsResponse(errorType, null)
            }
            try {
                println(req.access_level)
                val insertStatement = conn.prepareCall(
                        "update admin.role_column_grants set access_level = ?::admin.access_level, column_name = ?, modified_by = ?, role = ?, table_name = ?::admin.table_name where id = ? returning *"
                )
                insertStatement.setString(1, req.access_level)
                insertStatement.setString(2, req.column_name)
                insertStatement.setInt(3, userId)
                insertStatement.setInt(4, req.role)
                insertStatement.setString(5, req.table_name)
                insertStatement.setInt(6, req.id)


                val insertStatementResult = insertStatement.executeQuery()

            }
            catch (e:SQLException ){
                println(e.message)
                errorType = ErrorType.SQLUpdateError
                return UpdateGlobalRoleColumnGrantsResponse(errorType, null)
            }
        }
        return UpdateGlobalRoleColumnGrantsResponse(ErrorType.NoError,insertedGlobalRole)
    }
}
