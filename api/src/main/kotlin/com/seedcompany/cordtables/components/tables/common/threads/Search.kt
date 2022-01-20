package com.seedcompany.cordtables.components.tables.common.threads

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.admin.people.Sensitivities
import com.seedcompany.cordtables.components.tables.admin.people.people
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.SQLException
import javax.sql.DataSource

data class SearchRequest(
  val searchColumnName: String,
  val token: String,
  val searchKeyword: String = ""
)

data class SearchResponse(
  val error: ErrorType,
  val data: MutableList<Thread>? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ThreadSearch")
class Search(
  @Autowired
  val util: Utility,
  @Autowired
  val secureList: GetSecureListQuery,
  @Autowired
  val ds: DataSource
){
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("common/threads/search")
  @ResponseBody
  fun searchHandler(@RequestBody req: SearchRequest): SearchResponse {
    if (req.token == null) return SearchResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return SearchResponse(ErrorType.AdminOnly)
    var data: MutableList<Thread> = mutableListOf()
    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)
    val whereClause = "${req.searchColumnName} like '${req.searchKeyword}%'"
    paramSource.addValue("token", req.token)
    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "common.threads",
//        filter = "order by id",
        columns = arrayOf(
          "id",
          "content",
          "channel",
          "created_at",
          "created_by",
          "modified_at",
          "modified_by",
          "owning_person",
          "owning_group",
        ),
        whereClause = whereClause
      )
    ).query
    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var id: String? = jdbcResult.getString("id")
        if (jdbcResult.wasNull()) id = null

        var content: String? = jdbcResult.getString("content")
        if (jdbcResult.wasNull()) content = null

        var channel: String? = jdbcResult.getString("channel")
        if (jdbcResult.wasNull()) channel = null

        var created_at: String? = jdbcResult.getString("created_at")
        if (jdbcResult.wasNull()) created_at = null

        var created_by: String? = jdbcResult.getString("created_by")
        if (jdbcResult.wasNull()) created_by = null

        var modified_at: String? = jdbcResult.getString("modified_at")
        if (jdbcResult.wasNull()) modified_at = null

        var modified_by: String? = jdbcResult.getString("modified_by")
        if (jdbcResult.wasNull()) modified_by = null

        var owning_person: String? = jdbcResult.getString("owning_person")
        if (jdbcResult.wasNull()) owning_person = null

        var owning_group: String? = jdbcResult.getString("owning_group")
        if (jdbcResult.wasNull()) owning_group = null

        data.add(
          Thread(
            id = id,
            channel = channel,
            content = content,
            created_at = created_at,
            created_by = created_by,
            modified_at = modified_at,
            modified_by = modified_by,
            owning_person = owning_person,
            owning_group = owning_group
          )
        )


      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return SearchResponse(ErrorType.SQLReadError)
    }
    return SearchResponse(ErrorType.NoError, data)
  }

}
