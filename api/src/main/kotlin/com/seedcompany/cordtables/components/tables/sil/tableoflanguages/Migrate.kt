package com.seedcompany.cordtables.components.tables.sil.tableoflanguages

import com.seedcompany.cordtables.common.CordApiRestUtils
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.core.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource

data class ValueInt(
    val value: Int? = null,
)

data class ValueString(
    val value: String? = null,
)

data class Ethnologue(
    val code: ValueString? = null,
    val name: ValueString? = null,
    val population: ValueInt? = null,
    val provisionalCode: ValueString? = null,
    val sensitivity: String? = null,
)

data class Item(
    val id: String? = null,
    val ethnologue: Ethnologue? = null,
)

data class Languages(
    val items: List<Item>? = listOf(),
    val total: Int? = null,
)

data class Data(
    val languages: Languages? = null,
)

data class Response(
    val data: Data? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilTableOfLanguagesMigrate")
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

    init {

        val token = cord.login()

        if (token != null) {

            // get count of ethnologue entries
            val countQuery = """{"query":"query Query {languages {total}}"}"""
            val countResponse = cord.post<Response>(token, countQuery)
            val total = countResponse?.data?.languages?.total

            if (total != null) {
                println("total languages: $total")

                // iterate over all languages and ensure we have them in postgres
                for (i in 1..total) {

                    val ethQuery = """
                        {"query":"query Query {languages(input: { page: $i, count: 1 }) {items {id ethnologue {code {value}name {value}population {value} provisionalCode {value} sensitivity}}}}"}
                    """.trimIndent()

                    val ethResponse = cord.post<Response>(token, ethQuery)

                    val eth = ethResponse?.data?.languages?.items?.get(0)?.ethnologue
                    if (eth != null) {

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

    }
}