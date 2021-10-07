package com.seedcompany.cordspringstencil.components.user

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class GlobalRolesTablePermissions(
    val id: Int,
    val createdAt: String,
    val createdBy: Int,
    val modifiedAt: String,
    val modifiedBy: Int,
    val tableName: String,
    val tablePermission: String,
    val globalRole: Int

)

data class ReadPermissionsResponse(
    val error: ErrorType,
    val data: MutableList<GlobalRolesTablePermissions>?
    // val token: String? = null,
)

@CrossOrigin(origins = ["http://localhostt:3333"])
@Controller()
class Read (
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource
) {
    @PostMapping("globalrolestablepermissions/read")
    @ResponseBody
    fun ReadHandler(): ReadPermissionsResponse {

        var data: MutableList<GlobalRolesTablePermissions> = mutableListOf()

        this.ds.connection.use { conn ->
            val listStatement = conn.prepareCall(
                "select id, created_at, created_by, modified_at, modified_by, table_name, table_permission, global_role from public.global_roles_table_permissions;"
            )
            try {

            val listStatementResult = listStatement.executeQuery()
            while (listStatementResult.next()) {
                val id = listStatementResult.getInt("id")
                val createdAt = listStatementResult.getString("created_at")
                val createdBy = listStatementResult.getInt("created_by")
                val modifiedBy = listStatementResult.getInt("modified_by")
                val modifiedAt = listStatementResult.getString("modified_at")
                val tableName = listStatementResult.getString("table_name")
                val tablePermissions = listStatementResult.getString("table_permissions")
                val globalRole = listStatementResult.getInt("global_role")
                data.add(GlobalRolesTablePermissions(id,createdAt,createdBy,modifiedAt,modifiedBy,tableName,tablePermissions,globalRole))
            }
        }
        catch (e:SQLException){
            println("error while listing ${e.message}")
            return ReadPermissionsResponse(ErrorType.SQLReadError, null)

             }
        }
        return ReadPermissionsResponse(ErrorType.NoError,data)
    }
}