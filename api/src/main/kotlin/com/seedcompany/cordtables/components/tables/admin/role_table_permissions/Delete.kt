package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

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

data class dataId(
    val id: Int?
)

data class DeletePermissionResponse(
    val error: ErrorType,
    val data: dataId?
)

data class DeletePermissionRequest(
    val id: Int
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("DeleteTablePermissions")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("globalrolestablepermissions/delete")
    @ResponseBody
    fun DeleteHandler(@RequestBody req: DeletePermissionRequest): DeletePermissionResponse {

        println("req: $req")
        var deletedPermissionRowId: Int?= null

        this.ds.connection.use { conn ->

            try {


                val deleteStatement = conn.prepareCall(
                    "delete from admin.role_table_permissions where id = ? returning id"
                )

//                modified_by, modified_at, id
                deleteStatement.setInt(1, req.id)
                val deleteStatementResult = deleteStatement.executeQuery()
//
                if (deleteStatementResult.next()) {
                    deletedPermissionRowId= deleteStatementResult.getInt("id")
                    println("deleted row's id: $deletedPermissionRowId")
                }
            } catch (e: SQLException) {
                println(e.message)
                return DeletePermissionResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return DeletePermissionResponse(ErrorType.NoError, dataId(deletedPermissionRowId))
    }
}