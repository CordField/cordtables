package com.seedcompany.cordtables.components.tables.sc.change_to_plans

import com.seedcompany.cordtables.components.tables.sc.change_to_plans.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.ScChangeToPlansDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScChangeToPlansDeleteRequest(
  val id: String,
  val token: String?,
)

data class ScChangeToPlansDeleteResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScChangeToPlansDelete")
class Delete(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/change-to-plans/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: ScChangeToPlansDeleteRequest): ScChangeToPlansDeleteResponse {

    if (req.token == null) return ScChangeToPlansDeleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScChangeToPlansDeleteResponse(ErrorType.AdminOnly)

    if (!util.userHasDeletePermission(req.token, "sc.change_to_plans"))
      return ScChangeToPlansDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    println("req: $req")
    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from sc.change_to_plans where id = ? returning id"
        )
        deleteStatement.setString(1, req.id)

        deleteStatement.setString(1, req.id)


        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return ScChangeToPlansDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }

    return ScChangeToPlansDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
