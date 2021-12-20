package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
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


data class CommonCountPeopleTicketsRequest(
  val token: String?,
  val wordToSearch: String?
)

data class CommonCountPeopleTicketsResponse(
  val error: ErrorType,
  val total_people: MutableList<CommonCountPeopleQuantity>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CountPeopleTickets")
class CountPeopleTickets(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {

  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("common-tickets/count-people-tickets")
  @ResponseBody
  fun listHandler(@RequestBody req: CommonCountPeopleTicketsRequest): CommonCountPeopleTicketsResponse{
    var data: MutableList<CommonCountPeopleQuantity> = mutableListOf()
    if (req.token == null) return CommonCountPeopleTicketsResponse(ErrorType.TokenNotFound, mutableListOf())

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)

    val filter  = if(req.wordToSearch.isNullOrBlank()) "" else "where public_full_name LIKE '%' || '${req.wordToSearch}' || '%' "

    val query = "select count(id) from admin.people $filter"

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var total: Int? = jdbcResult.getInt("count")
        if (jdbcResult.wasNull()) total = null

        data.add(
          CommonCountPeopleQuantity(
            total = total
          )
        )
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return CommonCountPeopleTicketsResponse(ErrorType.SQLReadError, mutableListOf())
    }
    return CommonCountPeopleTicketsResponse(ErrorType.NoError, data)
  }
}
