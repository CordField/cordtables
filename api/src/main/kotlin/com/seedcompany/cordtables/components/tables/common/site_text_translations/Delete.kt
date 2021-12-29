package com.seedcompany.cordtables.components.tables.common.site_text_translations

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

data class SiteTextTranslationDeleteRequest(
  val id: String,
  val token: String?,
)

data class SiteTextTranslationDeleteResponse(
  val error: ErrorType,
  val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextTranslationDelete")
class Delete(
  @Autowired
  val util: Utility,
  @Autowired
  val ds: DataSource,
) {
  @PostMapping("common-site-text-translations/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: SiteTextTranslationDeleteRequest): SiteTextTranslationDeleteResponse {

    if (req.token == null) return SiteTextTranslationDeleteResponse(ErrorType.TokenNotFound, null)
    if (!util.userHasDeletePermission(req.token, "common.site_text_translations"))
      return SiteTextTranslationDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    var deletedLocationExId: String? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from common.site_text_translations where id = ?::uuid returning id"
        )
        deleteStatement.setString(1, req.id)

        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedLocationExId = deleteStatementResult.getString("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return SiteTextTranslationDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }
    return SiteTextTranslationDeleteResponse(ErrorType.NoError, deletedLocationExId)
  }
}
