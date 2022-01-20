package com.seedcompany.cordtables.components.tables.common.blog_posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonBlogPostsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonBlogPostsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonBlogPostsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/blog-posts/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonBlogPostsUpdateRequest): CommonBlogPostsUpdateResponse {

      if (req.token == null) return CommonBlogPostsUpdateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return CommonBlogPostsUpdateResponse(ErrorType.AdminOnly)
        if (req.column == null) return CommonBlogPostsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonBlogPostsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "group_id" -> {
                util.updateField(
                    token = req.token,
                    table = "common.blog_posts",
                    column = "blog",
                    id = req.id,
                    value = req.value
                )
            }
            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.blog_posts",
                    column = "content",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.blog_posts",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.blog_posts",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return CommonBlogPostsUpdateResponse(ErrorType.NoError)
    }

}
