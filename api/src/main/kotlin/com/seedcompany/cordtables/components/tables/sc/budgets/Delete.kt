package com.seedcompany.cordtables.components.tables.sc.budgets

import com.seedcompany.cordtables.components.tables.sc.budgets.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.budgets.ScBudgetsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScBudgetsDeleteRequest(
  val id: String,
  val token: String?,
)

data class ScBudgetsDeleteResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetsDelete")
class Delete(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/budgets/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: ScBudgetsDeleteRequest): ScBudgetsDeleteResponse {

    if (req.token == null) return ScBudgetsDeleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScBudgetsDeleteResponse(ErrorType.AdminOnly)

    if (!util.userHasDeletePermission(req.token, "sc.budgets"))
      return ScBudgetsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    println("req: $req")
    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from sc.budgets where id = ? returning id"
        )
        deleteStatement.setString(1, req.id)

        deleteStatement.setString(1, req.id)


        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return ScBudgetsDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }

    return ScBudgetsDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
