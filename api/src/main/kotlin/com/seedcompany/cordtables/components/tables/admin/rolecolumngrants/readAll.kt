package com.seedcompany.cordtables.components.tables.globalrolecolumngrants
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.apache.tomcat.jni.Global
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class GlobalRoleColumnGrants(
        val id : Int? = null,
        val access_level: String? = null,
        val column_name: String? = null,
        val created_at: String? = null,
        val created_by: Int? = null,
        val role: Int? = null,
        val modified_at: String? = null,
        val modified_by: Int? = null,
        val table_name: String? = null,
)

data class GlobalRoleColumnGrantsReturn(
        val error : ErrorType,
        val response: MutableList<GlobalRoleColumnGrants>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@RestController("globalRoleColumnGrantsReadAllController")
class readAll(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {


    @PostMapping(path = ["table/global-role-column-grants"], consumes = ["application/json"], produces = ["application/json"])

    @ResponseBody
    fun GlobalRoleColumnGrantsHandler(): GlobalRoleColumnGrantsReturn {

        var response: MutableList<GlobalRoleColumnGrants> = mutableListOf()

        this.ds.connection.use { conn ->
            val listStatement = conn.prepareCall("SELECT\n" +
                    "\tid, \n" +
                    "\taccess_level, \n" +
                    "\tcolumn_name, \n" +
                    "\tcreated_at,\n" +
                    "\tcreated_by,\n" +
                    "\trole,\n" +
                    "\tmodified_at,\n" +
                    "\tmodified_by,\n" +
                    "\ttable_name\n" +
                    "FROM admin.role_column_grants")
            try {
                val listStatementResult = listStatement.executeQuery();
                while (listStatementResult.next()) {
                    val id = listStatementResult.getInt("id");
                    val access_level = listStatementResult.getString("access_level");
                    val column_name = listStatementResult.getString("column_name");
                    val created_at = listStatementResult.getString("created_at");
                    val created_by = listStatementResult.getInt("created_by");
                    val role = listStatementResult.getInt("role");
                    val modified_at = listStatementResult.getString("modified_at");
                    val modified_by = listStatementResult.getInt("modified_by");
                    val table_name = listStatementResult.getString("table_name");
                    response.add(GlobalRoleColumnGrants(id, access_level, column_name, created_at, created_by, role, modified_at, modified_by, table_name));
                }
            } catch (e: SQLException) {
                println("error while getting ${e.message}")
                return GlobalRoleColumnGrantsReturn(ErrorType.emptyReadResult, null);
            }



            return GlobalRoleColumnGrantsReturn(ErrorType.NoError, response)
        }
    }
}