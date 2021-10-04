package com.seedcompany.cordtables.components.user

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class GlobalRole(
    val id: Int,
    val createdAt: String,
    val createdBy: Int,
    val modifiedAt: String,
    val modifiedBy: Int,
    val name: String,
    val org: Int
)

data class ReadGlobalRolesResponse(
    val error: ErrorType,
    val data: MutableList<GlobalRole>?
)

@CrossOrigin(origins = ["http://localhost:3333"])
@Controller()
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("globalroles/read")
    @ResponseBody
    fun ReadHandler(): ReadGlobalRolesResponse {
        //mutableList as we need to add each global role as an element to it
        var data: MutableList<GlobalRole> = mutableListOf()


        this.ds.connection.use { conn ->
            val listStatement = conn.prepareCall(
                "select id, created_at, created_by,modified_at,modified_by, name, org from public.global_roles;"
            )
            try {
                val listStatementResult = listStatement.executeQuery()
                while (listStatementResult.next()) {

                    val id = listStatementResult.getInt("id")
                    val name = listStatementResult.getString("name")
                    val createdBy = listStatementResult.getInt("created_by")
                    val modifiedBy = listStatementResult.getInt("modified_by")
                    val org = listStatementResult.getInt("org")
                    val createdAt = listStatementResult.getString("created_at")
                    val modifiedAt = listStatementResult.getString("modified_at")
                    data.add(GlobalRole(id, createdAt, createdBy, modifiedAt, modifiedBy, name, org))
                }
            }
            catch(e:SQLException){
                println("error while listing ${e.message}")
                return ReadGlobalRolesResponse(ErrorType.SQLReadError, null)
            }
        }
        return ReadGlobalRolesResponse(ErrorType.NoError,data)
    }
}