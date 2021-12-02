package com.seedcompany.cordtables.components.tables.admin.peers

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.core.AppConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import javax.sql.DataSource

data class PeerInitRequest(
    val url: String? = null,
    val sourceToken: String? = null,
)

data class PeerInitReturn(
    val error: ErrorType,
    val targetToken: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminPeersInit")
class Init(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val rest: RestTemplate,

    @Autowired
    val appConfig: AppConfig,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("admin/peers/init")
    @ResponseBody
    fun initHandler(@RequestBody req: PeerInitRequest): PeerInitReturn {

        println(req)

        var errorType: ErrorType? = null

        var thisSourceToken = util.createToken()

        this.ds.connection.use { conn ->

            //language=SQL
            val checkNameStatement = conn.prepareStatement(
                """
                select exists(select id from admin.peers where url = ?)
            """.trimIndent()
            )

            checkNameStatement.setString(1, req.url)

            val result = checkNameStatement.executeQuery();

            if (result.next()) {
                val urlFound = result.getBoolean(1)

                if (urlFound) {
                    return PeerInitReturn(ErrorType.PeerAlreadyExists)
                } else {

                    //language=SQL
                    val statement = conn.prepareStatement(
                        """
                            insert into admin.peers(url, source_token, target_token, created_by, modified_by, owning_person, owning_group) 
                                values (?, ?, ?, 1, 1, 1, 1);
                        """.trimIndent()
                    )

                    statement.setString(1, req.url)
                    statement.setString(2, thisSourceToken)
                    statement.setString(3, req.sourceToken) // their source token is our target token

                    statement.execute()

                }
            }


        }

        var confirmResponse: PeerConfirmReturn? = null
        try {
            confirmResponse = rest.postForObject<PeerConfirmReturn>(
                "${req.url}/admin/peers/confirm",
                PeerConfirmRequest(url = appConfig.thisServerUrl, sourceToken = req.sourceToken, targetToken = thisSourceToken)
            )
        } catch (e: Exception) {
            println("confirm post failed")
            return PeerInitReturn(ErrorType.PeerFailedToConfirm)
        }

        if (confirmResponse == null) {
            println("confirmResponse was null")
            return PeerInitReturn(ErrorType.PeerFailedToConfirm)
        }

        if (confirmResponse.error == ErrorType.NoError) {
            val person = jdbcTemplate.queryForObject(
                """
                insert into admin.people(sensitivity_clearance) values ('Low') returning id;
            """.trimIndent(),
                Int::class.java,
            )

            jdbcTemplate.update(
                """
                    update admin.peers
                    set url_confirmed = true,
                        person = ?
                    where url = ?;
                """.trimIndent(),
                person,
                req.url,
            )

        } else {
            println("Init fn: Confirm response: ${confirmResponse.error}")
            return PeerInitReturn(ErrorType.PeerFailedToConfirm)
        }


        var loginResponse: PeerLoginReturn? = null
        try {
            loginResponse = rest.postForObject<PeerLoginReturn>(
                "${req.url}/admin/peers/login",
                PeerLoginRequest(url = appConfig.thisServerUrl, targetToken = thisSourceToken)
            )
        } catch (e: Exception) {
            println("failed to login peer")
            return PeerInitReturn(ErrorType.PeerFailedToLogin)
        }

        if (loginResponse == null) {
            println("peer login response was null")
            return PeerInitReturn(ErrorType.PeerFailedToLogin)
        }

        if (loginResponse.sessionToken == null || loginResponse.sessionToken?.length !== 64) {
            return PeerInitReturn(ErrorType.PeerFailedToLogin)
        }

        jdbcTemplate.update(
            """
                update admin.peers
                set session_token = ?
                where url = ?;
            """.trimIndent(),
            loginResponse.sessionToken,
            req.url,
        )


        return PeerInitReturn(ErrorType.NoError, targetToken = thisSourceToken)
    }

}