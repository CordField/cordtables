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
import javax.sql.DataSource
import kotlin.reflect.full.memberProperties
import kotlin.collections.mutableListOf as mutableListOf

data class dataId(
    val id: Int?
)

data class DeleteGlobalRoleResponse(
    val error: ErrorType,
    val data: dataId?
)

data class DeleteGlobalRoleRequest(
    val id: Int
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller()
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("globalroles/delete")
    @ResponseBody
    fun DeleteHandler(@RequestBody req: DeleteGlobalRoleRequest): DeleteGlobalRoleResponse {

        println("req: $req")
        var deletedGlobalRowId: Int?= null
        var userId = 0


        this.ds.connection.use { conn ->
//            try {
//                val getUserIdStatement = conn.prepareCall("select person from common.users where email = ?")
//                getUserIdStatement.setString(1, req.email)
//                val getUserIdResult = getUserIdStatement.executeQuery()
//                if (getUserIdResult.next()) {
//                    userId = getUserIdResult.getInt("person")
//                    println("userId: $userId")
//                } else {
//                    throw SQLException("User not found")
//                }
//            } catch (e: SQLException) {
//                println(e.message)
//                return UpdateGlobalRoleResponse(ErrorType.UserNotFound, null)
//            }
            try {


                val deleteStatement = conn.prepareCall(
                    "delete from admin.global_roles where id = ? returning id"
                )

//                modified_by, modified_at, id
                deleteStatement.setInt(1, req.id)
                val deleteStatementResult = deleteStatement.executeQuery()
//
                if (deleteStatementResult.next()) {
                    deletedGlobalRowId= deleteStatementResult.getInt("id")
                    println("deleted row's id: $deletedGlobalRowId")
                }
            } catch (e: SQLException) {
                println(e.message)
                return DeleteGlobalRoleResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return DeleteGlobalRoleResponse(ErrorType.NoError, dataId(deletedGlobalRowId))
    }
}
