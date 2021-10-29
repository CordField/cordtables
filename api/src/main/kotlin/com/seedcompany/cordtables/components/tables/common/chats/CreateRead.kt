package com.seedcompany.cordtables.components.tables.common.chats

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.chats.CommonChatsCreateReadRequest
import com.seedcompany.cordtables.components.tables.common.chats.CommonChatsCreateReadResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonChatsCreateReadRequest(
    val token: String? = null,
    val chat: ChatInput,
)

data class CommonChatsCreateReadResponse(
    val error: ErrorType,
    val chat: Chat? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonChatsCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("common-chats/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonChatsCreateReadRequest): CommonChatsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonChatsCreateRequest(
                token = req.token,
                chat = req.chat
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonChatsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonChatsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonChatsCreateReadResponse(error = readResponse.error, chat = readResponse.chat)
    }
}
