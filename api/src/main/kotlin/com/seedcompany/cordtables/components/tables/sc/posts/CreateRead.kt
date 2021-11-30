package com.seedcompany.cordtables.components.tables.sc.posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.posts.*
import com.seedcompany.cordtables.components.tables.sc.posts.ScPostsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.posts.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPostsCreateReadRequest(
    val token: String? = null,
    val post: postInput,
)

data class ScPostsCreateReadResponse(
    val error: ErrorType,
    val post: post? = null,
)


@Controller("ScPostsCreateRead")
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
    @PostMapping("sc-posts/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScPostsCreateReadRequest): ScPostsCreateReadResponse {

        val createResponse = create.createHandler(
            ScPostsCreateRequest(
                token = req.token,
                post = req.post
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScPostsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScPostsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScPostsCreateReadResponse(error = readResponse.error, post = readResponse.post)
    }
}