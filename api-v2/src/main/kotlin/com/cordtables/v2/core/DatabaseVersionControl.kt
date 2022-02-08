package com.cordtables.v2.core

import com.cordtables.v2.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import javax.sql.DataSource

@Component
class DatabaseVersionControl(

    @Autowired
    val appConfig: AppConfig,

    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val jdbc: JdbcTemplate,
) {
    init {
        if (!isDbInit()) {
            toVersion1()
            if (appConfig.loadDbVersion > 1) {
                updateSchemaIdempotent(appConfig.loadDbVersion)
            }
            updateHistoryTables()
            updatePeerTables()
            println("database schema init complete")
        } else {
            println("db is ready")
        }
    }

    private fun isDbInit(): Boolean {
        return jdbc.queryForObject(
            """
               SELECT EXISTS (
               SELECT FROM information_schema.tables
               where    table_name   = 'database_version_control_x'
               );
            """.trimIndent(),
            Boolean::class.java,
        ) ?: false
    }

    private fun getSchemaVersion(): Int {
        return jdbc.queryForObject(
            """
                select version 
                from database_version_control_x 
                order by version 
                desc limit 1;
            """.trimIndent(),
            Int::class.java
        ) ?: 0
    }

    fun updateSchemaIdempotent(version: Int) {
        while (getSchemaVersion() < version) {
            when (getSchemaVersion()) {
//                1 -> {
//                    println("upgrading schema to version 2")
//                    toVersion2()
//                }
//                2 -> {
//                    println("upgrading schema to version 3")
//                    toVersion3()
//                }
//                3 -> {
//                    println("upgrading schema to version 4")
//                    toVersion4()
//                }
                else -> {
                    break
                }
            }
        }

    }

    private fun updateHistoryTables() {
        runSqlFile("sql/history.fn.sql")
    }

    private fun updatePeerTables() {
        runSqlFile("sql/peer.fn.sql")
    }

    private fun toVersion1() {
        println("version 1 not found. creating schema.")

        // admin
        runSqlFile("sql/admin.schema.v1.sql")

        // common
//        runSqlFile("sql/schemas/common/v1.sql")

        // sil
//        runSqlFile("sql/schemas/sil/sil.v1.sql")
//        runSqlFile("sql/migration/language_index.migration.sql")
//    runSqlFile("sql/migration/ethnologue.migration.sql")

        // up (unceasing prayer)
//        runSqlFile("sql/schemas/up/up.v1.sql")

        // load data functions
//        runSqlFile("sql/data/bootstrap.data.sql")

        // user
//        jdbc.execute("sql/modules/user/register.sql")
//        jdbc.execute("sql/modules/user/login.sql")

        // prep and bootstrap the db
//        val pash = util.encoder.encode(appConfig.cordAdminPassword)
//
//        var errorType = ErrorType.UnknownError
//
//        this.ds.connection.use { conn ->
//            val bootstrapStatement = conn.prepareCall("call bootstrap(?, ?, ?);")
//            bootstrapStatement.setString(1, "devops@tsco.org")
//            bootstrapStatement.setString(2, pash)
//            bootstrapStatement.setString(3, errorType.name)
//            bootstrapStatement.registerOutParameter(3, java.sql.Types.VARCHAR)
//
//            bootstrapStatement.execute()
//
//            try {
//                errorType = ErrorType.valueOf(bootstrapStatement.getString(3))
//            } catch (ex: IllegalArgumentException) {
//                errorType = ErrorType.UnknownError
//            }
//
//            if (errorType != ErrorType.NoError) {
//                println("bootstrap query failed")
//            }
//
//            bootstrapStatement.close()
//        }

        // update version control table
        setVersionNumber(1)

    }

    private fun setVersionNumber(newVersion: Int) {
        jdbc.update(
            """
                insert into database_version_control_x(version, status, started, completed)
                    values(?, 'Completed', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
            """.trimIndent(),
            newVersion
        )
    }

    private fun asString(resource: ClassPathResource): String? {
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

    private fun runSqlFile(fileName: String) {
        val sql = asString(ClassPathResource(fileName))
        if (sql !== null) {
            jdbc.execute(sql)
            println("$fileName successfully run")
        }
    }

}