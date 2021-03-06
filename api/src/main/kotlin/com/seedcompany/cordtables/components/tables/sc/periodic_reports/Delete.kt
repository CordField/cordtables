package com.seedcompany.cordtables.components.tables.sc.periodic_reports

import com.seedcompany.cordtables.components.tables.sc.periodic_reports.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.periodic_reports.ScPeriodicReportsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScPeriodicReportsDeleteRequest(
  val id: String,
  val token: String?,
)

data class ScPeriodicReportsDeleteResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPeriodicReportsDelete")
class Delete(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/periodic-reports/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: ScPeriodicReportsDeleteRequest): ScPeriodicReportsDeleteResponse {

    if (req.token == null) return ScPeriodicReportsDeleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScPeriodicReportsDeleteResponse(ErrorType.AdminOnly)

    if (!util.userHasDeletePermission(req.token, "sc.periodic_reports"))
      return ScPeriodicReportsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    println("req: $req")
    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from sc.periodic_reports where id = ? returning id"
        )
        deleteStatement.setString(1, req.id)

        deleteStatement.setString(1, req.id)


        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return ScPeriodicReportsDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }

    return ScPeriodicReportsDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
