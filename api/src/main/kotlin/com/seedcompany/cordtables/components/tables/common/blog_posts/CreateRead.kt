package com.seedcompany.cordtables.components.tables.common.blog_posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.blog_posts.*
import com.seedcompany.cordtables.components.tables.common.blog_posts.CommonBlogPostsCreateRequest
import com.seedcompany.cordtables.components.tables.common.blog_posts.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonBlogPostsCreateReadRequest(
    val token: String? = null,
    val blogPost: blogPostInput,
)

data class CommonBlogPostsCreateReadResponse(
    val error: ErrorType,
    val blogPost: blogPost? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonBlogPostsCreateRead")
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
    @PostMapping("common/blog-posts/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonBlogPostsCreateReadRequest): CommonBlogPostsCreateReadResponse {

      if (req.token == null) return CommonBlogPostsCreateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return CommonBlogPostsCreateReadResponse(ErrorType.AdminOnly)

        val createResponse = create.createHandler(
            CommonBlogPostsCreateRequest(
                token = req.token,
                blogPost = req.blogPost
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonBlogPostsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonBlogPostsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonBlogPostsCreateReadResponse(error = readResponse.error, blogPost = readResponse.blogPost)
    }
}
