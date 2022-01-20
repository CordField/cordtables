package com.seedcompany.cordtables.components.tables.common.blogs

import com.seedcompany.cordtables.components.tables.common.blogs.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.blogs.CommonBlogsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonBlogsDeleteRequest(
  val id: String,
  val token: String?,
)

data class CommonBlogsDeleteResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonBlogsDelete")
class Delete(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("common/blogs/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: CommonBlogsDeleteRequest): CommonBlogsDeleteResponse {

    if (req.token == null) return CommonBlogsDeleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return CommonBlogsDeleteResponse(ErrorType.AdminOnly)

    if (!util.userHasDeletePermission(req.token, "common.blogs"))
      return CommonBlogsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    println("req: $req")
    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from common.blogs where id = ?::uuid returning id"
        )
        deleteStatement.setString(1, req.id)

        deleteStatement.setString(1, req.id)


        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return CommonBlogsDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }

    return CommonBlogsDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
