package com.seedcompany.cordspringstencil.common

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
                	from public.global_roles 
                	where id in (
                		select global_role 
                		from public.global_role_memberships 
                		where person = (
                			select person
                			from public.tokens 
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
        var userHasCreatePermission: Boolean = false;
        this.ds.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareCall(
                """
                select exists(
                	select a.id 
                	from public.global_roles as a 
                    inner join public.global_role_table_permissions as b 
                    on a.id = b.global_role 
                	where a.id in (
                		select global_role 
                		from public.global_role_memberships
                		where person = (
                			select person
                			from public.tokens 
                			where token = ?
                        )
                    ) 
                   and b.table_name = ?
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


}
