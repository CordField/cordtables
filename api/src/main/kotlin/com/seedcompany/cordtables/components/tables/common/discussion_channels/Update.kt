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

data class CommonDiscussionChannelsUpdateRequest(
    val token: String?,
    val discussionchannel: DiscussionChannelInput? = null,
)

data class CommonDiscussionChannelsUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonDiscussionChannelsUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-discussion-channels/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonDiscussionChannelsUpdateRequest): CommonDiscussionChannelsUpdateResponse {

        if (req.token == null) return CommonDiscussionChannelsUpdateResponse(ErrorType.TokenNotFound)
        if (req.discussionchannel == null) return CommonDiscussionChannelsUpdateResponse(ErrorType.MissingId)
        if (req.discussionchannel.id == null) return CommonDiscussionChannelsUpdateResponse(ErrorType.MissingId)

        if (req.discussionchannel.owning_person != null) util.updateField(
                token = req.token,
                table = "common.discussion_channels",
                column = "owning_person",
                id = req.discussionchannel.id!!,
                value = req.discussionchannel.owning_person,
        )

        if (req.discussionchannel.owning_group != null) util.updateField(
                token = req.token,
                table = "common.discussion_channels",
                column = "owning_group",
                id = req.discussionchannel.id!!,
                value = req.discussionchannel.owning_group,
        )

        return CommonDiscussionChannelsUpdateResponse(ErrorType.NoError)
    }

}
