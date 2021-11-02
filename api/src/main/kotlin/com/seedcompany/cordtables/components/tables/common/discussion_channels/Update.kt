package com.seedcompany.cordtables.components.tables.common.discussion_channels

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonChatsUpdateRequest(
    val token: String?,
    val chat: ChatInput? = null,
)

data class CommonChatsUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonChatsUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-discussion-channels/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonChatsUpdateRequest): CommonChatsUpdateResponse {

        if (req.token == null) return CommonChatsUpdateResponse(ErrorType.TokenNotFound)
        if (req.chat == null) return CommonChatsUpdateResponse(ErrorType.MissingId)
        if (req.chat.id == null) return CommonChatsUpdateResponse(ErrorType.MissingId)

        if (req.chat.owning_person != null) util.updateField(
                token = req.token,
                table = "common.discussion_channels",
                column = "owning_person",
                id = req.chat.id!!,
                value = req.chat.owning_person,
        )

        if (req.chat.owning_group != null) util.updateField(
                token = req.token,
                table = "common.discussion_channels",
                column = "owning_group",
                id = req.chat.id!!,
                value = req.chat.owning_group,
        )

        return CommonChatsUpdateResponse(ErrorType.NoError)
    }

}
