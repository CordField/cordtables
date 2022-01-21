package com.seedcompany.cordtables.components.tables.common.blog_posts

import com.seedcompany.cordtables.components.tables.common.blog_posts.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.blog_posts.CommonBlogPostsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonBlogPostsDeleteRequest(
  val id: String,
  val token: String?,
)

data class CommonBlogPostsDeleteResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonBlogPostsDelete")
class Delete(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("common/blog-posts/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: CommonBlogPostsDeleteRequest): CommonBlogPostsDeleteResponse {

    if (req.token == null) return CommonBlogPostsDeleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return CommonBlogPostsDeleteResponse(ErrorType.AdminOnly)

    if (!util.userHasDeletePermission(req.token, "common.blog_posts"))
      return CommonBlogPostsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    println("req: $req")
    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from common.blog_posts where id = ? returning id"
        )
        deleteStatement.setString(1, req.id)

        deleteStatement.setString(1, req.id)


        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return CommonBlogPostsDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }

    return CommonBlogPostsDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
