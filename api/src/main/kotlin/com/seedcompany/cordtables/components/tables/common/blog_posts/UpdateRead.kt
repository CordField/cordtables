package com.seedcompany.cordtables.components.tables.common.blog_posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.blog_posts.CommonBlogPostsReadRequest
import com.seedcompany.cordtables.components.tables.common.blog_posts.CommonBlogPostsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.blog_posts.blogPost
import com.seedcompany.cordtables.components.tables.common.blog_posts.blogPostInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonBlogPostsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonBlogPostsUpdateReadResponse(
    val error: ErrorType,
    val blogPost: blogPost? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonBlogPostsUpdateRead")
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
    @PostMapping("common/blog-posts/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonBlogPostsUpdateReadRequest): CommonBlogPostsUpdateReadResponse {

      if (req.token == null) return CommonBlogPostsUpdateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return CommonBlogPostsUpdateReadResponse(ErrorType.AdminOnly)

        val updateResponse = update.updateHandler(
            CommonBlogPostsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonBlogPostsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonBlogPostsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonBlogPostsUpdateReadResponse(error = readResponse.error, readResponse.blogPost)
    }
}
