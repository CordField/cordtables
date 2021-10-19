package com.seedcompany.cordtables.components.tables.sc.languages

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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource

data class ValueInt(
    val value: Int? = null,
)

data class ValueString(
    val value: String? = null,
)

data class ValueBoolean(
    val value: Boolean? = null,
)

data class Ethnologue(
    val code: ValueString? = null,
    val name: ValueString? = null,
    val population: ValueInt? = null,
    val provisionalCode: ValueString? = null,
    val sensitivity: String? = null,
)



data class Language(
    val id: String? = null,
    val name: ValueString? = null,
    val displayName: ValueString? = null,
    val displayNamePronunciation: ValueString? = null,
    val isDialect: ValueBoolean? = null,
    val populationOverride: ValueInt? = null,
    val registryOfDialectsCode: ValueString? = null,
    val leastOfThese: ValueBoolean? = null,
    val leastOfTheseReason: ValueString? = null,
    val signLanguageCode:

)

data class Languages(
    val items: List<Language>? = listOf(),
    val total: Int? = null,
)

data class Data(
    val languages: Languages? = null,
)

data class Response(
    val data: Data? = null,
)

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


    @PostMapping("migrate/sc-language")
    @ResponseBody
    fun registerHandler() {

        val token = cord.login()

        if (token != null) {

            // get count of language entries
            val countQuery = """{"query":"query Query {languages {total}}"}"""
            val countResponse = cord.post<Response>(token, countQuery)
            val total = countResponse?.data?.languages?.total

            if (total == null) {
                println("language total was null")
                return
            }

            println("total languages: $total")

            // iterate over all languages and ensure we have them in postgres
            for (i in 1..total) {

                val langQuery = """
                        {"query":"query Query { languages { items { avatarLetters name { value } displayName { value } displayNamePronunciation { value } isDialect { value } populationOverride { value } registryOfDialectsCode { value } leastOfThese { value } leastOfTheseReason { value } signLanguageCode { value } sponsorEstimatedEndDate { value } sensitivity isSignLanguage { value } hasExternalFirstScripture { value } tags { value } presetInventory { value } population { value } } } }"}
                    """.trimIndent()

                val langResponse = cord.post<Response>(token, langQuery)

                val lang = langResponse?.data?.languages?.items?.get(0)
                if (lang == null) {
                    println("language entry was null")
                    continue
                }

                var errorType: ErrorType? = null

                this.ds.connection.use { conn ->
                    val migrationStatement = conn.prepareCall("call sc.sc_migrate_language(?,?,?,?,?,?);")
                    migrationStatement.setString(1, lang.code?.value)
                    migrationStatement.setString(2, lang.name?.value)
                    if (lang.population?.value != null) {
                        migrationStatement.setInt(3, lang.population?.value)
                    } else {
                        migrationStatement.setNull(3, java.sql.Types.NULL)
                    }
                    migrationStatement.setString(4, lang.provisionalCode?.value)
                    migrationStatement.setObject(5, lang.sensitivity, java.sql.Types.OTHER)
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

                println("Migrated ethnologue entry code: '${lang.code?.value}': $errorType")


            }


        }

    }
}