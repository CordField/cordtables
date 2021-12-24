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
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonDiscussionChannelsUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
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
        if (req.id == null) return CommonDiscussionChannelsUpdateResponse(ErrorType.MissingId)
        if (req.column == null) return CommonDiscussionChannelsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.discussion_channels",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.discussion_channels",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.discussion_channels",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return CommonDiscussionChannelsUpdateResponse(ErrorType.NoError)
    }

}
