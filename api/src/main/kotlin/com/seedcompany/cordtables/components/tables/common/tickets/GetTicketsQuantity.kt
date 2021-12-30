package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.CommonTicketStatus
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.languages.*
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


data class CommonCountTicketsRequest(
  val token: String?,
  val wordToSearch: String?
)

data class CommonCountTicketsResponse(
  val error: ErrorType,
  val total_tickets: MutableList<CommonCountTicketsQuantity>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCountTickets")
class CountTickets(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {

  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("common-tickets/count-tickets")
  @ResponseBody
  fun listHandler(@RequestBody req: CommonCountTicketsRequest): CommonCountTicketsResponse{
    var data: MutableList<CommonCountTicketsQuantity> = mutableListOf()
    if (req.token == null) return CommonCountTicketsResponse(ErrorType.TokenNotFound, mutableListOf())

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)

    val filter  = if(req.wordToSearch.isNullOrBlank()) "" else "where title LIKE '%' || '${req.wordToSearch}' || '%' "

    val query = "select count(id) from common.tickets $filter"

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var total: Int? = jdbcResult.getInt("count")
        if (jdbcResult.wasNull()) total = null

        data.add(
          CommonCountTicketsQuantity(
            total = total
          )
        )
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return CommonCountTicketsResponse(ErrorType.SQLReadError, mutableListOf())
    }
    return CommonCountTicketsResponse(ErrorType.NoError, data)
  }
}
