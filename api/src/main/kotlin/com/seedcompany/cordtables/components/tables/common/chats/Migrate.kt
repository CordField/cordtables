package com.seedcompany.cordtables.components.tables.common.chats

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
@Controller("CommonChatsMigrate")
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



    @PostMapping("migrate/common-chats")
    @ResponseBody
    fun registerHandler() {

        val token = cord.login()

        if (token != null) {

            // get count of chat entries
            val countQuery = """{"query":"query Query {chats {total}}"}"""
            val countResponse = cord.post<GResponse>(token, countQuery)
            val total = countResponse?.data?.chats?.total

            if (total == null) {
                println("chat total was null")
                return
            }

            println("total chats: $total")

            // iterate over all chats and ensure we have them in postgres
            for (i in 1..total) {

                val chatQuery = """
                        {"query":"query Query { chats(input: { page: $i, count: 1 }) { items { id channel { value } content { value } } } }"}
                    """.trimIndent()

                val chatResponse = cord.post<GResponse>(token, chatQuery)

                val chat = chatResponse?.data?.chats?.items?.get(0)

                if (chat == null) {
                    println("chat entry was null")
                    continue
                }

                var errorType: ErrorType? = null

                this.ds.connection.use { conn ->
                    val migrationStatement = conn.prepareCall("call common.common_migrate_chat(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);")
                    migrationStatement.setString(1, chat.id)
                    migrationStatement.setString(2, chat.channel)
                    migrationStatement.setString(3, chat.content)
                    migrationStatement.toString()
                    migrationStatement.execute()

                    try {
                        errorType = ErrorType.valueOf(migrationStatement.getString(18))
                    } catch (ex: IllegalArgumentException) {
                        errorType = ErrorType.UnknownError
                    }

                    if (errorType != ErrorType.NoError) {
                        println("chat migration query failed")
                    }

                    migrationStatement.close()
                }

                println("Migrated chat entry code: '${chat.id}': $errorType")


            }


        }

    }
}