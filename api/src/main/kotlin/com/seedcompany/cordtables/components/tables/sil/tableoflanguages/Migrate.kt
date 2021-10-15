package com.seedcompany.cordtables.components.tables.sil.tableoflanguages

import com.seedcompany.cordtables.common.CordApiRestUtils
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.core.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource

data class Languages(
    val total: Int?,
)

data class Data(
    val languages: Languages?,
)

data class Response(
    val data: Data,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("SilTableOfLanguagesMigrate")
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

    init {

        val token = cord.login()

        if (token != null) {

            // get count of ethnologue entries
            val countQuery = """{"query":"query Query {languages {total}}"}"""

            val countResponse = cord.post<Response>(token, countQuery)

            val total = countResponse?.data?.languages?.total

            println("total languages: $total")



        }

    }
}