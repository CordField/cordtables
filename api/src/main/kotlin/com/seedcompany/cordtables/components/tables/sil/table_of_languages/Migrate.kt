package com.seedcompany.cordtables.components.tables.sc.ethnologue

import com.seedcompany.cordtables.common.CordApiRestUtils
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.GResponse
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.core.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScEthnologueMigrate")
@DependsOn("BootstrapDB")
class Migrate(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val cord: CordApiRestUtils,

    @Autowired
    val rest: RestTemplate,

    @Autowired
    val appConfig: AppConfig,

    ) {
    val jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)
    val jdbcTemplate2 = JdbcTemplate(ds)


    @PostMapping("migrate/sc/ethnologue")
    @ResponseBody
    fun registerHandler() {
        val token = cord.login()

        if (token != null) {

            // get count of ethnologue entries
            val countQuery = """{"query":"query Query {languages {total}}"}"""
            val countResponse = cord.post<GResponse>(token, countQuery)
            val total = countResponse?.data?.languages?.total

            if (total == null) {
                println("language total was null")
                return
            }

            println("total languages: $total")

            // iterate over all languages and ensure we have them in postgres
            for (i in 1..total) {

                val ethQuery = """
                        {"query":"query Query {languages(input: { page: $i, count: 1 }) {items {id ethnologue {code {value}name {value}population {value} provisionalCode {value} sensitivity}}}}"}
                    """.trimIndent()

                val ethResponse = cord.post<GResponse>(token, ethQuery)

                val eth = ethResponse?.data?.languages?.items?.get(0)?.ethnologue
                if (eth == null) {
                    println("ethnologue entry was null")
                    continue
                }

                var errorType: ErrorType? = null

                this.ds.connection.use { conn ->
                    val migrationStatement = conn.prepareCall("call sc.sc_migrate_ethnologue(?,?,?,?,?,?);")
                    migrationStatement.setString(1, eth.code?.value)
                    migrationStatement.setString(2, eth.name?.value)
                    if (eth.population?.value != null) {
                        migrationStatement.setInt(3, eth.population?.value)
                    } else {
                        migrationStatement.setNull(3, java.sql.Types.NULL)
                    }
                    migrationStatement.setString(4, eth.provisionalCode?.value)
                    migrationStatement.setObject(5, eth.sensitivity, java.sql.Types.OTHER)
                    migrationStatement.setNull(6, java.sql.Types.NULL)
                    migrationStatement.registerOutParameter(6, java.sql.Types.VARCHAR)

                    migrationStatement.execute()

                    try {
                        errorType = ErrorType.valueOf(migrationStatement.getString(6))
                    } catch (ex: IllegalArgumentException) {
                        errorType = ErrorType.UnknownError
                    }

                    if (errorType != ErrorType.NoError) {
                        println("ethnologue migration query failed")
                    }

                    migrationStatement.close()
                }

                println("Migrated ethnologue entry code: '${eth.code?.value}': $errorType")


            }


        }

    }
}
