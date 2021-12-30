package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
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


data class CommonTicketsIdNameListRequest(
  val token: String?,
  val wordToSearch: String?,
  val limit: Number,
  val offset: Number
)

data class CommonTicketsIdNameListResponse(
  val error: ErrorType,
  val tickets: MutableList<CommmonTicketsIdTitles>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketsListIdTitle")
class ListAll(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {

  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("common-tickets/list-id-and-title")
  @ResponseBody
  fun listHandler(@RequestBody req: CommonTicketsIdNameListRequest): CommonTicketsIdNameListResponse{
    var data: MutableList<CommmonTicketsIdTitles> = mutableListOf()
    if (req.token == null) return CommonTicketsIdNameListResponse(ErrorType.TokenNotFound, mutableListOf())

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)

    val filter = if(req.wordToSearch.isNullOrBlank()) "" else "title LIKE '%' || '${req.wordToSearch}' || '%' "
    println(filter)

    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "common.tickets",
        whereClause = filter,
        filter =  "limit ${req.limit} offset ${req.offset}",
        columns = arrayOf(
          "id",
          "title"
        )
      )
    ).query

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var id: Int? = jdbcResult.getInt("id")
        if (jdbcResult.wasNull()) id = null

        var ticketTitle: String? = jdbcResult.getString("title")
        if (jdbcResult.wasNull()) ticketTitle = null


        data.add(
          CommmonTicketsIdTitles(
            id = id,
            title = ticketTitle
          )
        )
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return CommonTicketsIdNameListResponse(ErrorType.SQLReadError, mutableListOf())
    }
    return CommonTicketsIdNameListResponse(ErrorType.NoError, data)
  }
}
