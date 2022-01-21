package com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships

import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.ScBudgetRecordsPartnershipsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScBudgetRecordsPartnershipsDeleteRequest(
  val id: String,
  val token: String?,
)

data class ScBudgetRecordsPartnershipsDeleteResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetRecordsPartnershipsDelete")
class Delete(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/budget-records-partnerships/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: ScBudgetRecordsPartnershipsDeleteRequest): ScBudgetRecordsPartnershipsDeleteResponse {

    if (req.token == null) return ScBudgetRecordsPartnershipsDeleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScBudgetRecordsPartnershipsDeleteResponse(ErrorType.AdminOnly)

    if (!util.userHasDeletePermission(req.token, "sc.budget_records_partnerships"))
      return ScBudgetRecordsPartnershipsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    println("req: $req")
    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from sc.budget_records_partnerships where id = ? returning id"
        )
        deleteStatement.setString(1, req.id)

        deleteStatement.setString(1, req.id)


        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return ScBudgetRecordsPartnershipsDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }

    return ScBudgetRecordsPartnershipsDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
