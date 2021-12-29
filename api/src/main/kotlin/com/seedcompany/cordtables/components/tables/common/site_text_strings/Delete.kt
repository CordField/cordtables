package com.seedcompany.cordtables.components.tables.common.site_text_strings

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class SiteTextStringDeleteRequest(
  val id: String,
  val token: String?,
)

data class SiteTextStringDeleteResponse(
  val error: ErrorType,
  val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextStringDelete")
class Delete(
  @Autowired
  val util: Utility,
  @Autowired
  val ds: DataSource,
) {
  @PostMapping("common-site-text-strings/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: SiteTextStringDeleteRequest): SiteTextStringDeleteResponse {

    if (req.token == null) return SiteTextStringDeleteResponse(ErrorType.TokenNotFound, null)
    if (!util.userHasDeletePermission(req.token, "common.site_text_strings"))
      return SiteTextStringDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from common.site_text_strings where id = ?::uuid returning id"
        )
        deleteStatement.setString(1, req.id)

        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return SiteTextStringDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }
    return SiteTextStringDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
