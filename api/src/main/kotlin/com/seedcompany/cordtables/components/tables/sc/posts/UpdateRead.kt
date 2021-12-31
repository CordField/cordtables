package com.seedcompany.cordtables.components.tables.sc.posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.posts.ScPostsReadRequest
import com.seedcompany.cordtables.components.tables.sc.posts.ScPostsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.posts.post
import com.seedcompany.cordtables.components.tables.sc.posts.postInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPostsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPostsUpdateReadResponse(
    val error: ErrorType,
    val post: post? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPostsUpdateRead")
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
    @PostMapping("sc/posts/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScPostsUpdateReadRequest): ScPostsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScPostsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScPostsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScPostsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScPostsUpdateReadResponse(error = readResponse.error, readResponse.post)
    }
}
