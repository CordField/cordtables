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

enum class Sensitivity {
    Low,
    Medium,
    High,
}

data class PeerCreateRequest(
    val token: String? = null,
    val url: String? = null,
    var peerApproved: Boolean? = false,
    val sensitivityClearance: Sensitivity = Sensitivity.Low,
)

data class PeerCreateReturn(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminPeersCreate")
class Create(
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

    @PostMapping("admin/peers/create")
    @ResponseBody
    fun createHandler(@RequestBody req: PeerCreateRequest): PeerCreateReturn {

        println(req)

        if (req.token == null) return PeerCreateReturn(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return PeerCreateReturn(ErrorType.AdminOnly)

        if (req.url == null) return PeerCreateReturn(ErrorType.InputMissingUrl)
        if (req.url.isEmpty()) return PeerCreateReturn(ErrorType.UrlTooShort)
        if (req.url.length > 128) return PeerCreateReturn(ErrorType.UrlTooLong)

        if (req.peerApproved == null) req.peerApproved = false

        var errorType: ErrorType? = null

        var sourceToken = util.createToken()

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
                val nameFound = result.getBoolean(1)

                if (nameFound) {
                    return PeerCreateReturn(ErrorType.UrlAlreadyExists)
                } else {

                    //language=SQL
                    val statement = conn.prepareStatement(
                        """
                        insert into admin.peers(url, peer_approved, source_token, created_by, modified_by, owning_person, owning_group) 
                            values(?, ?, ?,
                                (
                                  select person 
                                  from admin.tokens 
                                  where token = ?
                                ),
                                (
                                  select person 
                                  from admin.tokens 
                                  where token = ?
                                ),
                                (
                                  select person 
                                  from admin.tokens 
                                  where token = ?
                                ),
                                1
                        );
                        """.trimIndent()
                    )

                    statement.setString(1, req.url)
                    statement.setBoolean(2, req.peerApproved!!)
                    statement.setString(3, sourceToken)
                    statement.setString(4, req.token)
                    statement.setString(5, req.token)
                    statement.setString(6, req.token)

                    statement.execute()

                }
            }
        }

        val person = jdbcTemplate.queryForObject(
            """
                insert into admin.people(sensitivity_clearance) values ('Low') returning id;
            """.trimIndent(),
            String::class.java,
        )

        jdbcTemplate.update(
            """
                update admin.peers
                set person = ?
                where url = ?
            """.trimIndent(),
            person,
            req.url,
        )

        var initResponse: PeerInitReturn? = null
        try {
            initResponse = rest.postForObject<PeerInitReturn>(
                "${req.url}/admin/peers/init",
                PeerInitRequest(url = appConfig.thisServerUrl, sourceToken = sourceToken)
            )
        } catch (e: Exception) {
            println("init post failed")
            return PeerCreateReturn(ErrorType.PeerFailedToInitialize)
        }

        if (initResponse == null) {
            println("initResponse was null")
            return PeerCreateReturn(ErrorType.PeerFailedToInitialize)
        }

        if (initResponse.error == ErrorType.NoError && initResponse.targetToken?.length == 64) {

        } else {
            println("initResponse: ${initResponse.error}")
            return PeerCreateReturn(initResponse.error)
        }

        return PeerCreateReturn(ErrorType.NoError)
    }

}
