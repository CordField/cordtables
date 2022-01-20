package com.seedcompany.cordtables.components.admin

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.people.SearchResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class AutocompleteRequest(
  val searchColumnName: String,
  val resultColumnName: String,
  val token: String,
  val searchKeyword: String,
  val tableName: String,
)

data class AutocompleteResponse(
  val error: ErrorType,
  val data: Any? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("Autocomplete")
class Autocomplete(
  @Autowired
  val util: Utility,
  @Autowired
  val secureList: GetSecureListQuery,
  @Autowired
  val ds: DataSource
) {
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("admin/autocomplete")
  @ResponseBody
  fun autocompleteHandler(@RequestBody req: AutocompleteRequest): AutocompleteResponse {
    if (req.token == null) return AutocompleteResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return AutocompleteResponse(ErrorType.AdminOnly)
    var resultColumnData: String?;
    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)
    val whereClause = "${req.searchColumnName} = '${req.searchKeyword}'"
    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = req.tableName,
//        filter = "order by id",
        columns = arrayOf(
          req.resultColumnName
        ),
        whereClause = whereClause
      )
    ).query
    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {
        resultColumnData = jdbcResult.getString(req.resultColumnName)
        if (jdbcResult.wasNull()) resultColumnData = null
        return AutocompleteResponse(ErrorType.NoError, resultColumnData)
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return AutocompleteResponse(ErrorType.SQLReadError)
    }
    return AutocompleteResponse(ErrorType.UnknownError)
  }

}
