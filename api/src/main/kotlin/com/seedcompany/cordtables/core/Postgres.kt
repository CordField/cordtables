package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Component
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import javax.sql.DataSource

@Component("BootstrapDB")
class BootstrapDB(
    @Autowired
    val appConfig: AppConfig,

    @Autowired
    val ds: DataSource,

    @Autowired
    val util: Utility,

) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    init {

        // VERSION 1 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //language=SQL
        val isVersion1 = jdbcTemplate.queryForObject<Boolean>(
            """
       SELECT EXISTS (
       SELECT FROM information_schema.tables 
       WHERE  table_schema = 'admin'
       AND    table_name   = 'global_roles'
       );
      """.trimIndent()
        )

        if (isVersion1) {
            println("version 1 found")
        } else {
            println("version 1 not found. creating schema.")

            // admin
            runSqlFile("sql/schemas/admin/admin.schema.sql")
            runSqlFile("sql/schemas/admin/admin.history.sql")

            // common
            runSqlFile("sql/schemas/common/common.schema.sql")
            runSqlFile("sql/schemas/common/common.history.sql")

            // sil
            runSqlFile("sql/schemas/sil/sil.schema.sql")
            runSqlFile("sql/schemas/sil/sil.history.sql")

            // sc
            runSqlFile("sql/schemas/sc/sc.schema.sql")
            runSqlFile("sql/schemas/sc/sc.history.sql")
            runSqlFile("sql/schemas/sc/ethnologue.migration.sql")

            // bootstrap
            runSqlFile("sql/version-control/bootstrap.sql")

            // db version control
            runSqlFile("sql/version-control/roles.migration.sql")

            // user
            runSqlFile("sql/modules/user/register.sql")
            runSqlFile("sql/modules/user/login.sql")

            // bootstrap
            val pash = util.encoder.encode(appConfig.cordAdminPassword)

            var errorType = ErrorType.UnknownError

            this.ds.connection.use { conn ->
                val bootstrapStatement = conn.prepareCall("call bootstrap(?, ?, ?);")
                bootstrapStatement.setString(1, "devops@tsco.org")
                bootstrapStatement.setString(2, pash)
                bootstrapStatement.setString(3, errorType.name)
                bootstrapStatement.registerOutParameter(3, java.sql.Types.VARCHAR)

                bootstrapStatement.execute()

                try {
                    errorType = ErrorType.valueOf(bootstrapStatement.getString(3))
                } catch (ex: IllegalArgumentException) {
                    errorType = ErrorType.UnknownError
                }

                if (errorType != ErrorType.NoError) {
                    println("bootstrap query failed")
                }

                bootstrapStatement.close()
            }

            jdbcTemplate.execute("call roles_migration();")
        }

    }

    fun asString(resource: ClassPathResource): String? {
        try {
            InputStreamReader(resource.inputStream, Charsets.UTF_8).use { reader ->
                return FileCopyUtils.copyToString(
                    reader
                )
            }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

    fun runSqlFile(fileName: String) {

        val sql = asString(ClassPathResource(fileName))
        if (sql !== null) {
            jdbcTemplate.execute(sql)
            println("$fileName successfully run")
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
