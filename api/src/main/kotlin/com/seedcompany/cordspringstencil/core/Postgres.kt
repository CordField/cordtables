package com.seedcompany.cordspringstencil.core

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component
import java.io.File
import javax.sql.DataSource

@Component("BootstrapDB")
class BootstrapDB(
    @Autowired
    val appConfig: AppConfig,

    @Autowired
    val ds: DataSource,

    @Autowired
    val util: Utility,

//  @Autowired
//  val r: ResourcePatternResolver,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    init {

        // VERSION 1 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //language=SQL
        val isVersion1 = jdbcTemplate.queryForObject<Boolean>(
            """
       SELECT EXISTS (
       SELECT FROM information_schema.tables 
       WHERE  table_schema = 'public'
       AND    table_name   = 'global_roles'
       );
      """.trimIndent()
        )

        if (isVersion1) {
            println("version 1 found")
        } else {
            println("version 1 not found. creating schema.")

            // schema
            runSqlFile("./src/main/kotlin/com/seedcompany/cordspringstencil/core/sql/sys_schema.sql")
            runSqlFile("./src/main/kotlin/com/seedcompany/cordspringstencil/core/sql/sc_schema.sql")
            runSqlFile("./src/main/kotlin/com/seedcompany/cordspringstencil/core/sql/bootstrap.sql")

            // user
            runSqlFile("./src/main/kotlin/com/seedcompany/cordspringstencil/components/user/register.sql")
            runSqlFile("./src/main/kotlin/com/seedcompany/cordspringstencil/components/user/login.sql")

            // bootstrap
            val pash = util.encoder.encode(appConfig.cordAdminPassword)

            var errorType = ErrorType.UnknownError

            this.ds.connection.use { conn ->
                val statement = conn.prepareCall("call bootstrap(?, ?, ?);")
                statement.setString(1, "devops@tsco.org")
                statement.setString(2, pash)
                statement.setString(3, errorType.name)
                statement.registerOutParameter(3, java.sql.Types.VARCHAR)

                statement.execute()

                try {
                    errorType = ErrorType.valueOf(statement.getString(3))
                } catch (ex: IllegalArgumentException) {
                    errorType = ErrorType.UnknownError
                }

                if (errorType != ErrorType.NoError) {
                    println("register query failed")
                }

                statement.close()
            }
        }

    }

    fun runSqlFile(fileName: String) {
        val sql = File(fileName).readText()
        if (sql != null) {
            jdbcTemplate.execute(sql)
            println("$fileName successfully ran")
        }
    }
}

@Configuration
class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource")
    fun cfDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}
