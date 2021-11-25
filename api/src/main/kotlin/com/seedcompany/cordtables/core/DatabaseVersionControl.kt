package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Component
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import javax.sql.DataSource
import java.io.BufferedReader

@Component
class DatabaseVersionControl(
    @Autowired
    val appConfig: AppConfig,

    @Autowired
    val ds: DataSource,

    @Autowired
    val util: Utility,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    fun updateDatabaseSchemaIdempotent() {
        if (!isVersion1()) toVersion1()
        updateSchemaIdempotent()
        println("database schema update complete")
        updateHistoryTables()
        updatePeerTables()
    }

    private fun updateSchemaIdempotent() {
        while (true) {
            when (getSchemaVersion()) {
                1 -> {
                    println("upgrading schema to version 2")
                    toVersion2()
                }
                else -> {
                    break
                }
            }
        }

    }

    private fun updateHistoryTables(){
        runSqlFile("sql/version-control/history.sql")
    }

    private fun updatePeerTables(){
        runSqlFile("sql/version-control/peer.sql")
    }

    private fun toVersion2() {
        // admin
        runSqlFile("sql/schemas/admin/admin.v2.sql")

        // common
        runSqlFile("sql/schemas/common/common.v2.sql")

        // sc
        runSqlFile("sql/schemas/sc/sc.v2.sql")
        setVersionNumber(2)
    }

    private fun getSchemaVersion(): Int? {
        return jdbcTemplate.queryForObject(
            """
                select version 
                from admin.database_version_control 
                order by version 
                desc limit 1;
            """.trimIndent()
        )
    }

    private fun isVersion1(): Boolean {
        return jdbcTemplate.queryForObject(
            """
               SELECT EXISTS (
               SELECT FROM information_schema.tables 
               WHERE  table_schema = 'admin'
               AND    table_name   = 'database_version_control'
               );
            """.trimIndent()
        )
    }

    private fun toVersion1() {
        println("version 1 not found. creating schema.")

        // admin
        runSqlFile("sql/schemas/admin/admin.v1.sql")

        // common
        runSqlFile("sql/schemas/common/common.v1.sql")

        // sil
        runSqlFile("sql/schemas/sil/sil.v1.sql")
        runSqlFile("sql/migration/language_index.migration.sql")

        // sc
        runSqlFile("sql/schemas/sc/sc.v1.sql")
        runSqlFile("sql/migration/ethnologue.migration.sql")

        // load data functions
        runSqlFile("sql/data/bootstrap.data.sql")
        runSqlFile("sql/data/roles.data.sql")

        // user
        runSqlFile("sql/modules/user/register.sql")
        runSqlFile("sql/modules/user/login.sql")

        // prep and bootstrap the db
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

        // load roles
        jdbcTemplate.execute("call roles_migration();")

        // load sil data if requested
        if (appConfig.loadLanguageData) loadSilData()

        if (appConfig.thisServerUrl == "http://localhost:8080"){
           // runSqlFile("sql/data/dummy.data.sql")
        }

        // update version control table
        jdbcTemplate.execute(
            """
            insert into admin.database_version_control(version, status, started, completed)
                values(1, 'Completed', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
            """.trimIndent()
        )

    }

    public fun loadSilData(){
        var adminPeopleId = 0
        println("here loadSil Data")
        this.ds.connection.use { conn ->
            try {

                val getAdminIdStatement =
                    conn.prepareCall("select id from admin.people where sensitivity_clearance = 'High'")

                val result = getAdminIdStatement.executeQuery()
                if (result.next()) {
                    adminPeopleId = result.getInt("id")
                }
                getAdminIdStatement.close()
            } catch (ex: IllegalArgumentException) {
                println("admin people not found!")
            }
        }

        // ====================  CountryCodes.tab load ===========================

        var readBuffer = BufferedReader(InputStreamReader(ClassPathResource("data/CountryCodes.tab").inputStream))

        var countryCodesQuery =
            "insert into sil.country_codes(country, name, area, created_by, modified_by, owning_person, owning_group) values "

        var count = 0
        var text: List<String> = readBuffer.readLines()
        for (line in text) {
            val splitArray = line.split("\t")
            val countryIdEntry = splitArray[0]
            val nameEntry = splitArray[1]
            val areaEntry = splitArray[2]
            count++
            if (count == 1) continue
            countryCodesQuery += "('${countryIdEntry}', '${nameEntry}', '${areaEntry}', ${adminPeopleId}, ${adminPeopleId}, ${adminPeopleId}, ${adminPeopleId}), "
        }

        countryCodesQuery = countryCodesQuery.dropLast(2) + ";"

        try {
            runSqlString(countryCodesQuery)
            println("CountryCodes.tab load success")
        } catch (ex: Exception) {
            println(ex)
            println("CountryCodes.tab load filed")
        }

        // ====================  LanguageCodes.tab load ===========================

        readBuffer = BufferedReader(InputStreamReader(ClassPathResource("data/LanguageCodes.tab").inputStream))

        var languageCodesQuery =
            "insert into sil.language_codes(lang, country, lang_status, name, created_by, modified_by, owning_person, owning_group) values "
        count = 0
        text = readBuffer.readLines()

        for (line in text) {
            val splitArray = line.split("\t")
            val lang = splitArray[0]
            val country = splitArray[1]
            val langStatus = splitArray[2]
            val name = splitArray[3]
            count++
            if (count == 1) continue
            languageCodesQuery += "('${lang}', '${country}', '${langStatus}', '${name}', ${adminPeopleId}, ${adminPeopleId}, ${adminPeopleId}, ${adminPeopleId}), "
        }
        languageCodesQuery = languageCodesQuery.dropLast(2) + ";"

        try {
            runSqlString(languageCodesQuery)
            println("LanguageCodes.tab load success")
        } catch (ex: Exception) {
            println(ex)

            println("LanguageCodes.tab load filed")
        }

        // ====================  LanguageIndex.tab load ===========================

        readBuffer = BufferedReader(InputStreamReader(ClassPathResource("data/LanguageIndex.tab").inputStream))

        count = 0
        text = readBuffer.readLines()
        var languageIndexQuery = ""
        for (line in text) {
            val splitArray = line.split("\t")
            val lang = splitArray[0]
            val country = splitArray[1]
            val nameType = splitArray[2]
            val name = splitArray[3]

            count++
            if (count == 1) continue
            languageIndexQuery += """
                call sil.sil_migrate_language_index('${lang}', '${country}', '${nameType}', '$name'); 
            """.trimIndent()
        }

        try {
            runSqlString(languageIndexQuery)
            println("LanguageIndex.tab load success")
        } catch (ex: Exception) {
            println(ex)
            println("LanguageIndex.tab load filed")
        }
    }

    private fun setVersionNumber(newVersion: Int) {
        jdbcTemplate.update(
            """
                insert into admin.database_version_control(version, status, started, completed)
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
            jdbcTemplate.execute(sql)
            println("$fileName successfully run")
        }
    }

    private fun runSqlString(sql: String) {
        jdbcTemplate.execute(sql)
    }
}