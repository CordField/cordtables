package com.seedcompany.cordtables.components.tables.sc.language_locations

import com.seedcompany.cordtables.components.tables.sc.language_locations.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.language_locations.ScLanguageLocationsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScLanguageLocationsDeleteRequest(
  val id: String,
  val token: String?,
)

data class ScLanguageLocationsDeleteResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageLocationsDelete")
class Delete(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/language-locations/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: ScLanguageLocationsDeleteRequest): ScLanguageLocationsDeleteResponse {

    if (req.token == null) return ScLanguageLocationsDeleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScLanguageLocationsDeleteResponse(ErrorType.AdminOnly)

    if (!util.userHasDeletePermission(req.token, "sc.language_locations"))
      return ScLanguageLocationsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    println("req: $req")
    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from sc.language_locations where id = ?::uuid returning id"
        )
        deleteStatement.setString(1, req.id)

        deleteStatement.setString(1, req.id)


        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return ScLanguageLocationsDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }

    return ScLanguageLocationsDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
