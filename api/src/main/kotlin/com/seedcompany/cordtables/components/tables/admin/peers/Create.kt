package com.seedcompany.cordtables.components.tables.admin.peers

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class PeerCreateRequest(
    val token: String? = null,
    val url: String? = null,
    val secret: String? = null,
    var approved: Boolean? = false,
)

data class PeerCreateReturn(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminPeersCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("admin/peers/create")
    @ResponseBody
    fun createHandler(@RequestBody req: PeerCreateRequest): PeerCreateReturn {

        if (req.token == null) return PeerCreateReturn(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return PeerCreateReturn(ErrorType.AdminOnly)

        if (req.url == null) return PeerCreateReturn(ErrorType.InputMissingUrl)
        if (req.url.isEmpty()) return PeerCreateReturn(ErrorType.UrlTooShort)
        if (req.url.length > 128) return PeerCreateReturn(ErrorType.UrlTooLong)

        if (req.secret == null) return PeerCreateReturn(ErrorType.InputMissingSecret)
        if (req.secret.length != 64) return PeerCreateReturn(ErrorType.SecretNotValid)

        if (req.approved == null) req.approved = false

        var errorType: ErrorType? = null

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
                        insert into admin.peers(url, secret, approved, created_by, modified_by, owning_person, owning_group) 
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
                    statement.setString(2, req.secret)
                    statement.setBoolean(3, req.approved!!)
                    statement.setString(4, req.token)
                    statement.setString(5, req.token)
                    statement.setString(6, req.token)

                    statement.execute()

                }
            }


        }

        return PeerCreateReturn(ErrorType.NoError)
    }

}