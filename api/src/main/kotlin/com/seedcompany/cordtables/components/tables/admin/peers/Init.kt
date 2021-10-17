package com.seedcompany.cordtables.components.tables.admin.peers

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
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

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminPeersInit")
class Init(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val rest: RestTemplate,
) {

    @PostMapping("admin/peers/init")
    @ResponseBody
    fun initHandler(@RequestBody req: PeerInitRequest): PeerInitReturn {

        var errorType: ErrorType? = null

        var targetToken = util.createToken()

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
                    statement.setString(2, targetToken) // their target token is our source token
                    statement.setString(3, req.sourceToken) // their source token is our target token

                    statement.execute()

                }
            }


        }

        var confirmResponse: PeerConfirmReturn? = null
        try {
            confirmResponse = rest.postForObject<PeerConfirmReturn>(
                "${req.url}/admin/peers/confirm",
                PeerConfirmRequest(url = req.url, sourceToken = req.sourceToken)
            )
        } catch (e: Exception) {
            return PeerInitReturn(ErrorType.PeerFailedToConfirm)
        }

        if (confirmResponse == null) return PeerInitReturn(ErrorType.PeerFailedToConfirm)

        return if (confirmResponse.error == ErrorType.NoError) {
            PeerInitReturn(ErrorType.NoError)
        } else {
            PeerInitReturn(ErrorType.PeerFailedToConfirm)
        }

    }

}