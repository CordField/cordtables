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

data class CommonDiscussionChannelsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonDiscussionChannelsUpdateReadResponse(
    val error: ErrorType,
    val discussion_channel: DiscussionChannel? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonDiscussionChannelsUpdateRead")
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
    @PostMapping("common-discussion-channels/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonDiscussionChannelsUpdateReadRequest): CommonDiscussionChannelsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonDiscussionChannelsUpdateRequest(
                token = req.token,
                id = req.id,
                column = req.column,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonDiscussionChannelsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonDiscussionChannelsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonDiscussionChannelsUpdateReadResponse(error = readResponse.error, readResponse.discussion_channel)
    }
}
