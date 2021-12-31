package com.seedcompany.cordtables.components.tables.admin.peers

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource

data class PeerLoginRequest(
    val url: String? = null,
    val targetToken: String? = null,
)

data class PeerLoginReturn(
    val error: ErrorType,
    val sessionToken: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminPeersLogin")
class Login(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val rest: RestTemplate,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("admin/peers/login")
    @ResponseBody
    fun peerLoginHandler(@RequestBody req: PeerLoginRequest): PeerLoginReturn {

        println(req)

        var errorType: ErrorType? = null

        var person: String? = null
        try {
            person = jdbcTemplate.queryForObject(
                """
                    select person from admin.peers where url = ? and target_token = ? and peer_approved = true;
                """.trimIndent(),
                String::class.java,
                req.url,
                req.targetToken,
            )
        } catch (e: IncorrectResultSizeDataAccessException) {
            return PeerLoginReturn(ErrorType.BadCredentials)
        }

        if (person == null) return PeerLoginReturn(ErrorType.BadCredentials)

        val sessionToken = util.createToken()

        try {
            jdbcTemplate.update(
                """
                    insert into admin.tokens(token, person) values (?, ?);
                """.trimIndent(),
                sessionToken,
                person,
            )
        } catch (e: IncorrectResultSizeDataAccessException) {
            return PeerLoginReturn(ErrorType.UnknownError)
        }

        return PeerLoginReturn(ErrorType.NoError, sessionToken)
    }

}
