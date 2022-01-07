package com.seedcompany.cordtables.components.admin

import org.apache.commons.lang3.mutable.Mutable
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
  val tableName: String,
  val columnName: String,
  val token: String,
  val searchKeyword: String = ""
)

data class SearchResponse(
  var searchResults: MutableList<Any?>,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("Search")
class Search(
@Autowired
val secureList: GetSecureListQuery,
@Autowired
val ds: DataSource
){
  @PostMapping("admin/search")
  @ResponseBody
  fun getSecureListQueryHandler(@RequestBody req: SearchRequest): SearchResponse {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
    val response = SearchResponse(mutableListOf())
    val paramSource = MapSqlParameterSource()
    val whereClause = "${req.columnName} like '%${req.searchKeyword}'"
    paramSource.addValue("token", req.token)
    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = req.tableName,
        filter = "order by id",
        columns = arrayOf(
          req.columnName
        ),
        whereClause = whereClause
//        searchKeyword = searchKeyword
      )
    ).query
    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
//      var resultList = mutableListOf<Any?>()
      while (jdbcResult.next()) {
//        for none string types, we can have a switch case here instead
        var columnNameValue: Any? = jdbcResult.getString(req.columnName)
        if (jdbcResult.wasNull()) columnNameValue = null
        response.searchResults.add(columnNameValue)
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
    }
    return response
  }

}
