package com.seedcompany.cordtables.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component
import java.util.regex.Pattern
import javax.sql.DataSource


@Component
class Utility(
//    @Autowired
//    val appConfig: AppConfig,
    @Autowired
    val ds: DataSource,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
    val encoder = Argon2PasswordEncoder(16, 32, 1, 4096, 3)

    //language=SQL
    val getUserIdFromSessionIdQuery = """
        select user_id from sessions where session_id = ?;
    """.trimIndent()

    fun getBearer(): String {
        return "todo"
    }

    fun createToken(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        val token = (1..64)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

        return token
    }

    fun getUserIdFromSessionId(sessionId: String): Int? {
        //language=SQL
        val userId: Int? = jdbcTemplate.queryForObject(
            """
          select user_id from sessions where session_id = ?;
      """.trimIndent(),
            Int::class.java,
            sessionId,
        )

        return userId
    }

    fun isEmailValid(email: String?): Boolean {

        if (email == null) return false

        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    fun isAdmin(token: String): Boolean {

        var isAdmin = false

        this.ds.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareCall(
                """
                select exists(
                	select id 
                	from admin.global_roles 
                	where id in (
                		select global_role 
                		from admin.global_role_memberships 
                		where person = (
                			select person
                			from admin.tokens 
                			where token = ?
                        )
                    ) 
                	and name = 'Administrator'
                );
            """.trimIndent()
            )

            statement.setString(1, token);
            val result = statement.executeQuery()

            if (result.next()) {
                isAdmin = result.getBoolean(1)
            }
        }

        return isAdmin;
    }

    fun userHasCreatePermission(token: String, tableName: String):Boolean {
        if(isAdmin(token)){
            return true;
        }
        var userHasCreatePermission: Boolean = false;
        this.ds.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareCall(
                """
                select exists(
                	select a.id 
                	from admin.global_roles as a 
                    inner join admin.global_role_table_permissions as b 
                    on a.id = b.global_role 
                	where a.id in (
                		select global_role 
                		from admin.global_role_memberships
                		where person = (
                			select person
                			from admin.tokens 
                			where token = ?
                        )
                    ) 
                   and b.table_name::text = ?
                   and b.table_permission = 'Create'
                );
            """.trimIndent()
            )

            statement.setString(1, token);
            statement.setString(2,tableName);
            val result = statement.executeQuery()

            if (result.next()) {
                userHasCreatePermission = result.getBoolean(1)
            }
        }
        return userHasCreatePermission;
    }

    fun userHasDeletePermission(token: String, tableName: String):Boolean {
        if(isAdmin(token)){
            return true;
        }
        var userHasDeletePermission: Boolean = false;
        this.ds.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareCall(
                """
                select exists(
                	select a.id 
                	from admin.global_roles as a 
                    inner join admin.global_role_table_permissions as b 
                    on a.id = b.global_role 
                	where a.id in (
                		select global_role 
                		from admin.global_role_memberships
                		where person = (
                			select person
                			from admin.tokens 
                			where token = ?
                        )
                    ) 
                   and b.table_name::text = ?
                   and b.table_permission = 'Delete'
                );
            """.trimIndent()
            )

            statement.setString(1, token);
            statement.setString(2,tableName);
            val result = statement.executeQuery()

            if (result.next()) {
                userHasDeletePermission = result.getBoolean(1)
            }
        }
        return userHasDeletePermission;
    }
    fun userHasUpdatePermission(token:String,tableName: String, columnNames:MutableList<String>):Boolean{
        if(isAdmin(token)){
            return true;
        }

        var userHasUpdatePermission = false;
        this.ds.connection.use { conn ->
            var updateSql = "select count(*) from \n" +
                    "(select column_name from admin.global_role_column_grants as a \n" +
                    "inner join admin.global_roles as b  \n" +
                    "on a.global_role = b.id \n" +
                    "where b.id in (\n" +
                    "\tselect global_role \n" +
                    "    from admin.global_role_memberships\n" +
                    "\twhere person = (\n" +
                    "\t\t\t\t\tselect person\n" +
                    "\t\t\t\t\tfrom admin.tokens \n" +
                    "\t\t\t\t\twhere token = ?\n" +
                    "                    )\n" +
                    ")\n" +
                    " and a.table_name::text = ? \n" +
                    " and a.access_level = 'Write' \n" +
                    " and a.column_name in ( ''\n"
//            need to add a empty column above so that when no update is sent, the query doesn't give an error for an empty subquery in check
            columnNames.forEach { columnName ->
                updateSql = "$updateSql '$columnName',"
            }
            updateSql = updateSql.dropLast(1)
            updateSql = "$updateSql ))sq"
            //language=SQL
            println(updateSql)
            val statement = conn.prepareCall("$updateSql ;")
            statement.setString(1, token);
            statement.setString(2,tableName);
            val result = statement.executeQuery()

            if (result.next()) {
                val countOfColumnsWithWriteAccess = result.getInt(1)
                if(countOfColumnsWithWriteAccess == columnNames.size){
                    userHasUpdatePermission = true
                }
            }
        }
        return userHasUpdatePermission;

    }

    fun getReadableTables(token: String):MutableList<String>{
        println("token $token")
        val tableNames = mutableListOf<String>()
        if(isAdmin(token)){
            this.ds.connection.use{conn->
                val statement = conn.prepareCall("select table_schema || '.' || table_name as table_name " +
                        "from information_schema.tables where table_schema in ('admin', 'common', 'sc', 'sil') order by table_name asc")
                val result = statement.executeQuery()
                while(result.next()){
                    tableNames.add(result.getString("table_name").replace('_','-'))
                }
            }
            return tableNames;
        }

        this.ds.connection.use{ conn ->
            val statement = conn.prepareCall("select distinct table_name \n" +
                    "from admin.global_role_column_grants as a \n" +
                    "inner join admin.global_role_memberships as b \n" +
                    "on a.global_role = b.global_role \n" +
                    "inner join admin.tokens as c \n" +
                    "on b.person = c.person \n" +
                    "where c.token = ? order by table_name asc")
            statement.setString(1,token)
            val result = statement.executeQuery()
            while(result.next()){
                tableNames.add(result.getString("table_name").replace('_','-'))
            }
            println("accessible tables: $tableNames")
        }
        return tableNames;
    }

}
