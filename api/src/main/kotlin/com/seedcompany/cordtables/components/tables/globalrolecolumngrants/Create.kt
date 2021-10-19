package com.seedcompany.cordtables.components.tables.globalrolecolumngrants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class GlobalRoleColumnGrantsCreate(
        val id: Int? = null,
        val access_level: String,
        val column_name: String? = null,
        val created_by: Int,
        val global_role: Int,
        val table_name: String? = null,
        val token: String
)

data class CreateGlobalRoleColumnGrantsResponse(
        val error: ErrorType,
        val response: GlobalRoleColumnGrantsCreate?
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@RestController("globalRoleColumnGrantsCreateController")
class Create(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("table/global-role-column-grants-create", consumes = ["application/json"], produces = ["application/json"])
    @ResponseBody
    fun CreateHandler(@RequestBody req: GlobalRoleColumnGrantsCreate): CreateGlobalRoleColumnGrantsResponse {

        println("req: $req")
        var errorType = ErrorType.UnknownError
        var insertedGlobalRole: GlobalRoleColumnGrantsCreate? = null
        var userId = 0

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
                return CreateGlobalRoleColumnGrantsResponse(errorType, null)
            }
            try {
                println(req.access_level)
                val insertStatement = conn.prepareCall(
                        "insert into admin.global_role_column_grants(access_level, column_name, created_by, modified_by, owning_person, owning_group, global_role, table_name) values(?::admin.access_level,?,?,?,?,?,?,?::admin.table_name) returning *"
                )
                insertStatement.setString(1, req.access_level)
                insertStatement.setString(2, req.column_name)
                insertStatement.setInt(3, userId)
                insertStatement.setInt(4, userId)
                insertStatement.setInt(5, userId)
                insertStatement.setInt(6, 1)
                insertStatement.setInt(7, req.global_role)
                insertStatement.setString(8, req.table_name)


                val insertStatementResult = insertStatement.executeQuery()

            }
            catch (e:SQLException ){
                println(e.message)
                errorType = ErrorType.SQLInsertError
                return CreateGlobalRoleColumnGrantsResponse(errorType, null)
            }
        }
        return CreateGlobalRoleColumnGrantsResponse(ErrorType.NoError,insertedGlobalRole)
    }
}
