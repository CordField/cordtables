package com.seedcompany.cordtables.components.tables.common.posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPostsCreateReadRequest(
        val token: String? = null,
        val post: PostInput,
)

data class CommonPostsCreateReadResponse(
        val error: ErrorType,
        val post: Post? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonPostsCreateRead")
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
    @PostMapping("common-posts/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonPostsCreateReadRequest): CommonPostsCreateReadResponse {

        val createResponse = create.createHandler(
                CommonPostsCreateRequest(
                        token = req.token,
                        post = req.post
                )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonPostsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
                CommonPostsReadRequest(
                        token = req.token,
                        id = createResponse!!.id
                )
        )

        return CommonPostsCreateReadResponse(error = readResponse.error, post = readResponse.post)
    }
}