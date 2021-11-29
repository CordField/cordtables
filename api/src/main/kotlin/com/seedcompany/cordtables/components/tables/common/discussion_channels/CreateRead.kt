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

data class CommonDiscussionChannelsCreateReadRequest(
    val token: String? = null,
    val discussion_channel: DiscussionChannelInput,
)

data class CommonDiscussionChannelsCreateReadResponse(
    val error: ErrorType,
    val discussion_channel: DiscussionChannel? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonDiscussionChannelsCreateRead")
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
    @PostMapping("common-discussion_channels/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonDiscussionChannelsCreateReadRequest): CommonDiscussionChannelsCreateReadResponse {

        val createResponse = create.createHandler(
            CommonDiscussionChannelsCreateRequest(
                token = req.token,
                discussion_channel = req.discussion_channel
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonDiscussionChannelsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonDiscussionChannelsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonDiscussionChannelsCreateReadResponse(error = readResponse.error, discussion_channel = readResponse.discussion_channel)
    }
}
