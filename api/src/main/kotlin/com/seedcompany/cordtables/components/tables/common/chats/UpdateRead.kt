package com.seedcompany.cordtables.components.tables.common.chats

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.chats.CommonChatsUpdateReadRequest
import com.seedcompany.cordtables.components.tables.common.chats.CommonChatsUpdateReadResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonChatsUpdateReadRequest(
    val token: String?,
    val chat: ChatInput? = null,
)

data class CommonChatsUpdateReadResponse(
    val error: ErrorType,
    val chat: Chat? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonChatsUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("common-chats/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonChatsUpdateReadRequest): CommonChatsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonChatsUpdateRequest(
                token = req.token,
                chat = req.chat,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonChatsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonChatsReadRequest(
                token = req.token,
                id = req.chat!!.id
            )
        )

        return CommonChatsUpdateReadResponse(error = readResponse.error, readResponse.chat)
    }
}
