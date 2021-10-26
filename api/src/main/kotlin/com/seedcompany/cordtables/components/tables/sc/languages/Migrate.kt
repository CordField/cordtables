package com.seedcompany.cordtables.components.tables.sc.languages

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
@Controller("ScLanguagesMigrate")
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



    @PostMapping("migrate/sc-languages")
    @ResponseBody
    fun registerHandler() {

        val token = cord.login()

        if (token != null) {

            // get count of language entries
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

                val langQuery = """
                        {"query":"query Query { languages(input: { page: $i, count: 1 }) { items { id ethnologue { code { value } } avatarLetters name { value } displayName { value } displayNamePronunciation { value } isDialect { value } populationOverride { value } registryOfDialectsCode { value } leastOfThese { value } leastOfTheseReason { value } signLanguageCode { value } sponsorEstimatedEndDate { value } sensitivity isSignLanguage { value } hasExternalFirstScripture { value } tags { value } presetInventory { value } population { value } } } }"}
                    """.trimIndent()

                val langResponse = cord.post<GResponse>(token, langQuery)

                val lang = langResponse?.data?.languages?.items?.get(0)

                if (lang == null) {
                    println("language entry was null")
                    continue
                }

                var errorType: ErrorType? = null

                this.ds.connection.use { conn ->
                    val migrationStatement = conn.prepareCall("call sc.sc_migrate_language(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);")
                    migrationStatement.setString(1, lang.ethnologue?.code?.value)
                    migrationStatement.setString(2, lang.id)
                    migrationStatement.setString(3, lang.name?.value)
                    migrationStatement.setString(4, lang.displayName?.value)
                    migrationStatement.setString(5, lang.displayNamePronunciation?.value)
                    if (lang.isDialect?.value == null)
                        migrationStatement.setNull(6, java.sql.Types.NULL)
                    else
                        migrationStatement.setBoolean(6, lang.isDialect?.value)
                    if (lang.populationOverride?.value == null) {
                        migrationStatement.setNull(7, java.sql.Types.NULL)
                    } else {
                        migrationStatement.setInt(7, lang.populationOverride?.value)
                    }

                    migrationStatement.setString(8, lang.registryOfDialectsCode?.value)

                    if(lang.leastOfThese?.value == null )
                        migrationStatement.setNull(9, java.sql.Types.NULL)
                    else
                        migrationStatement.setBoolean(9, lang.leastOfThese?.value)

                    migrationStatement.setString(10, lang.leastOfTheseReason?.value)
                    migrationStatement.setString(11, lang.signLanguageCode?.value)
                    migrationStatement.setTimestamp(12, lang.sponsorEstimatedEndDate?.value)
                    migrationStatement.setObject(13, lang.sensitivity, java.sql.Types.OTHER)

                    if(lang.isSignLanguage?.value == null)
                        migrationStatement.setNull(14, java.sql.Types.NULL)
                    else
                        migrationStatement.setBoolean(14, lang.isSignLanguage?.value)
                    if(lang.hasExternalFirstScripture?.value == null)
                        migrationStatement.setNull(15, java.sql.Types.NULL)
                    else
                        migrationStatement.setBoolean(15, lang.hasExternalFirstScripture?.value)
                    migrationStatement.setArray(16, conn.createArrayOf("text", lang.tags?.value))
                    if(lang.presetInventory?.value == null)
                        migrationStatement.setNull(17, java.sql.Types.NULL)
                    else
                        migrationStatement.setBoolean(17, lang.presetInventory?.value)

                    migrationStatement.setNull(18, java.sql.Types.NULL)
                    migrationStatement.registerOutParameter(18, java.sql.Types.VARCHAR)

                    migrationStatement.toString()
                    migrationStatement.execute()

                    try {
                        errorType = ErrorType.valueOf(migrationStatement.getString(18))
                    } catch (ex: IllegalArgumentException) {
                        errorType = ErrorType.UnknownError
                    }

                    if (errorType != ErrorType.NoError) {
                        println("language migration query failed")
                    }

                    migrationStatement.close()
                }

                println("Migrated language entry code: '${lang.id}': $errorType")


            }


        }

    }
}