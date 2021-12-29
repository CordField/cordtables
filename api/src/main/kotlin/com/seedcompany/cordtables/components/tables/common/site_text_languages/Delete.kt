package com.seedcompany.cordtables.components.tables.common.site_text_languages

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

data class SiteTextLanguageDeleteRequest(
  val language: Int,
  val token: String?,
)

data class SiteTextLanguageDeleteResponse(
  val error: ErrorType,
  val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextLanguageDelete")
class Delete(
  @Autowired
  val util: Utility,
  @Autowired
  val ds: DataSource,
) {
  @PostMapping("common/site-text-languages/delete")
  @ResponseBody
  fun deleteHandler(@RequestBody req: SiteTextLanguageDeleteRequest): SiteTextLanguageDeleteResponse {

    if (req.token == null) return SiteTextLanguageDeleteResponse(ErrorType.TokenNotFound, null)
    if (!util.userHasDeletePermission(req.token, "common.site_text_languages"))
      return SiteTextLanguageDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

    var deletedExId: Int? = null

    this.ds.connection.use { conn ->
      try {

        val deleteStatement = conn.prepareCall(
          "delete from common.site_text_languages where language = ? returning id"
        )
        deleteStatement.setInt(1, req.language)

        val deleteStatementResult = deleteStatement.executeQuery()

        if (deleteStatementResult.next()) {
          deletedExId = deleteStatementResult.getInt("id")
        }
      } catch (e: SQLException) {
        println(e.message)

        return SiteTextLanguageDeleteResponse(ErrorType.SQLDeleteError, null)
      }
    }
    return SiteTextLanguageDeleteResponse(ErrorType.NoError, deletedExId)
  }
}
