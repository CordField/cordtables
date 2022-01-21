package com.seedcompany.cordtables.components.tables.sc.project_members

import com.seedcompany.cordtables.components.tables.sc.project_members.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.project_members.ScProjectMembersDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScProjectMembersDeleteRequest(
  val id: String,
  val token: String?,
)

data class ScProjectMembersDeleteResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectMembersDelete")
class Delete(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/project-members/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: ScProjectMembersDeleteRequest): ScProjectMembersDeleteResponse {

    if (req.token == null) return ScProjectMembersDeleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScProjectMembersDeleteResponse(ErrorType.AdminOnly)

    if (!util.userHasDeletePermission(req.token, "sc.project_members"))
      return ScProjectMembersDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    println("req: $req")
    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from sc.project_members where id = ? returning id"
        )
        deleteStatement.setString(1, req.id)

        deleteStatement.setString(1, req.id)


        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return ScProjectMembersDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }

    return ScProjectMembersDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
