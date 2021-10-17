package com.seedcompany.cordtables.components.tables.admin.peers

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource

data class PeerConfirmRequest(
    val url: String? = null,
    val sourceToken: String? = null,
)

data class PeerConfirmReturn(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminPeersConfirm")
class Confirm(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val rest: RestTemplate,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("admin/peers/confirm")
    @ResponseBody
    fun confirmHandler(@RequestBody req: PeerConfirmRequest): PeerConfirmReturn {

        var errorType: ErrorType? = null

        this.ds.connection.use { conn ->

            println(req.sourceToken)
            println(req.url)

            //language=SQL
            val checkNameStatement = conn.prepareStatement(
                """
                select exists(select id from admin.peers where url = ? and source_token = ?)
            """.trimIndent()
            )

            checkNameStatement.setString(1, req.url)
            checkNameStatement.setString(2, req.sourceToken)

            val result = checkNameStatement.executeQuery();

            if (result.next()) {
                val urlFound = result.getBoolean(1)

                if (urlFound) {
                    jdbcTemplate.update("""
                        update admin.peers
                        set url_confirmed = true
                        where url = ?;
                    """.trimIndent(),
                        req.url,
                    )
                    return PeerConfirmReturn(ErrorType.NoError)
                } else {
                    println("peer not found")
                    return PeerConfirmReturn(ErrorType.PeerNotPresent)
                }
            }
        }

        return PeerConfirmReturn(ErrorType.UnknownError)
    }

}