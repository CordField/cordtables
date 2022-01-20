package com.seedcompany.cordtables.components.tables.sc.ceremonies

import com.seedcompany.cordtables.components.tables.sc.ceremonies.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.ceremonies.ScCeremoniesDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScCeremoniesDeleteRequest(
  val id: String,
  val token: String?,
)

data class ScCeremoniesDeleteResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScCeremoniesDelete")
class Delete(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("sc/ceremonies/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: ScCeremoniesDeleteRequest): ScCeremoniesDeleteResponse {

    if (req.token == null) return ScCeremoniesDeleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScCeremoniesDeleteResponse(ErrorType.AdminOnly)

    if (!util.userHasDeletePermission(req.token, "sc.ceremonies"))
      return ScCeremoniesDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    println("req: $req")
    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from sc.ceremonies where id = ?::uuid returning id"
        )
        deleteStatement.setString(1, req.id)

        deleteStatement.setString(1, req.id)


        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return ScCeremoniesDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }

    return ScCeremoniesDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
