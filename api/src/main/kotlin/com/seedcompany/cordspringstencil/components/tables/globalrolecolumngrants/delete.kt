package com.seedcompany.cordspringstencil.components.tables.globalrolecolumngrants

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class GlobalRoleColumnGrantsDelete(
        val id: Int,
        val token: String,
)

data class DeleteGlobalRoleColumnGrantsResponse(
        val error: ErrorType,
        val response: GlobalRoleColumnGrantsDelete?
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com" ])
@Controller()
class Delete(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("table/global-role-column-grants-delete")
    @ResponseBody
    fun DeleteHandler(@RequestBody req: GlobalRoleColumnGrantsDelete): DeleteGlobalRoleColumnGrantsResponse {

        println("req: $req")
        var errorType = ErrorType.UnknownError
        var deletedGlobalRole: GlobalRoleColumnGrantsDelete? = null
        var userId = 0

        this.ds.connection.use { conn ->
            try {
                val getUserIdStatement = conn.prepareCall("select person from public.tokens where token = ?")
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
                return DeleteGlobalRoleColumnGrantsResponse(errorType, null)
            }
            try {
                val insertStatement = conn.prepareCall(
                        "delete from public.global_role_column_grants where id = ?"
                )

                insertStatement.setInt(1, req.id)


                val insertStatementResult = insertStatement.executeQuery()

            }
            catch (e:SQLException ){
                println(e.message)
                errorType = ErrorType.SQLDeleteError
                return DeleteGlobalRoleColumnGrantsResponse(errorType, null)
            }
        }
        return DeleteGlobalRoleColumnGrantsResponse(ErrorType.NoError,deletedGlobalRole)
    }
}
