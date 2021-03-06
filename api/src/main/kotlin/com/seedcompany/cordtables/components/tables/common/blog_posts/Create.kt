package com.seedcompany.cordtables.components.tables.common.blog_posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.blog_posts.blogPostInput
import com.seedcompany.cordtables.components.tables.common.blog_posts.Read
import com.seedcompany.cordtables.components.tables.common.blog_posts.Update
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonBlogPostsCreateRequest(
    val token: String? = null,
    val blogPost: blogPostInput,
)

data class CommonBlogPostsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonBlogPostsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("common/blog-posts/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonBlogPostsCreateRequest): CommonBlogPostsCreateResponse {

      if (req.token == null) return CommonBlogPostsCreateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return CommonBlogPostsCreateResponse(ErrorType.AdminOnly)
        // if (req.blogPost.name == null) return CommonBlogPostsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.blog_posts(blog, content,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.blogPost.blog,
            req.blogPost.content,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

        return CommonBlogPostsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
