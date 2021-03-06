package com.seedcompany.cordtables.components.tables.common.discussion_channels

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonDiscussionChannelsDeleteRequest(
    val id: String,
    val token: String?,
)

data class CommonDiscussionChannelsDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonDiscussionChannelsDelete")
class Delete(
    @Autowired
    val util: Utility,
    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/discussion-channels/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonDiscussionChannelsDeleteRequest): CommonDiscussionChannelsDeleteResponse {

        if (req.token == null) return CommonDiscussionChannelsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.discussion_channels"))
            return CommonDiscussionChannelsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedDiscussionChannelExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.discussion_channels where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)

                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                  deletedDiscussionChannelExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return CommonDiscussionChannelsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonDiscussionChannelsDeleteResponse(ErrorType.NoError,deletedDiscussionChannelExId)
    }
}
