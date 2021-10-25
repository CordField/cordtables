package com.seedcompany.cordtables.components.tables.sc.location

import com.seedcompany.cordtables.common.CordApiRestUtils
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.*
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

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScLocationsMigrate")
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



    @PostMapping("migrate/sc-location")
    @ResponseBody
    fun registerHandler() {
        val token = cord.login()
        if (token != null) {

            // get count of language entries
            val countQuery = """{"query":"query Query {locations {total}}"}"""
            val countResponse = cord.post<GResponse>(token, countQuery)
            val total = countResponse?.data?.locations?.total

            if (total == null) {
                println("location total was null")
                return
            }

            println("total locations: $total")

            // iterate over all languages and ensure we have them in postgres
            for (i in 1..total) {

                val locQuery = """{"query":
                        "query Query {
                          locations(input: { page: $i, count:1 }) {
                            items {
                              id
                              fundingAccount {
                                value {
                                  id
                                }
                              }
                              defaultFieldRegion {
                                value {
                                  id
                                }
                              }
                              isoAlpha3 {
                                value
                              }
                              isoCountry {
                                numeric
                                country
                                alpha3
                                alpha2
                              }
                              name {
                                value
                              }
                              type {
                                value
                              }
                            }
                          }
                        }"
                    }
                    """.replace("\n", "").trimIndent()

                val locationResponse = cord.post<GResponse>(token, locQuery)

                val loc = locationResponse?.data?.locations?.items?.get(0)

                if (loc == null) {
                    println("location entry was null")
                    continue
                }

                var errorType: ErrorType? = null

                this.ds.connection.use { conn ->
                    val migrationStatement = conn.prepareCall("call sc.sc_migrate_location(?,?,?,?,?,?,?,?,?,?,?);")
                    migrationStatement.setString(1, loc.id)
                    migrationStatement.setString(2, loc.name?.value)
                    migrationStatement.setString(3, loc.defaultFieldRegion?.value?.id)
                    migrationStatement.setString(4, loc.fundingAccount?.value?.id)
                    migrationStatement.setString(5, loc.isoAlpha3?.value)
                    if (loc.isoCountry?.numeric != null)
                        migrationStatement.setInt(6, loc.isoCountry.numeric)
                    else
                        migrationStatement.setNull(6, java.sql.Types.NULL)
                    migrationStatement.setString(7, loc.isoCountry?.country)
                    migrationStatement.setString(8, loc.isoCountry?.alpha3)
                    migrationStatement.setString(9, loc.isoCountry?.alpha2)
                    migrationStatement.setObject(10, loc.type?.value, java.sql.Types.OTHER)
                    migrationStatement.setNull(11, java.sql.Types.NULL)
                    migrationStatement.registerOutParameter(11, java.sql.Types.VARCHAR)

                    migrationStatement.toString()
                    migrationStatement.execute()

                    try {
                        errorType = ErrorType.valueOf(migrationStatement.getString(11))
                    } catch (ex: IllegalArgumentException) {
                        errorType = ErrorType.UnknownError
                    }

                    if (errorType != ErrorType.NoError) {
                        println("location migration query failed")
                    }

                    migrationStatement.close()
                }

                println("Migrated location entry code: '${loc.id}/${loc.name?.value}': $errorType")


            }
        }
    }
}