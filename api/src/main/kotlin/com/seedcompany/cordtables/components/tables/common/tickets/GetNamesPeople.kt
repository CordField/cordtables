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


data class CommonPeopleIdNameListRequest(
  val token: String?,
  val wordToSearch: String?,
  val limit: Number,
  val offset: Number
)

data class CommonPeopleIdNameListResponse(
  val error: ErrorType,
  val people: MutableList<CommonPeopleNames>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleListNames")
class ListAllPeople(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,
) {

  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("common-tickets/list-people-names")
  @ResponseBody
  fun listHandler(@RequestBody req: CommonTicketsIdNameListRequest): CommonPeopleIdNameListResponse{
    var data: MutableList<CommonPeopleNames> = mutableListOf()
    if (req.token == null) return CommonPeopleIdNameListResponse(ErrorType.TokenNotFound, mutableListOf())

    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)

    val filter = if(req.wordToSearch.isNullOrBlank()) "" else "public_full_name LIKE '%' || '${req.wordToSearch}' || '%' "
    println(filter)

    val query = secureList.getSecureListQueryHandler(
      GetSecureListQueryRequest(
        tableName = "admin.people",
        whereClause = filter,
        filter =  "limit ${req.limit} offset ${req.offset}",
        columns = arrayOf(
          "id",
          "public_full_name"
        )
      )
    ).query

    try {
      val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
      while (jdbcResult.next()) {

        var id: Int? = jdbcResult.getInt("id")
        if (jdbcResult.wasNull()) id = null

        var name: String? = jdbcResult.getString("public_full_name")
        if (jdbcResult.wasNull()) name = null


        data.add(
          CommonPeopleNames(
            id = id,
            name = name
          )
        )
      }
    } catch (e: SQLException) {
      println("error while listing ${e.message}")
      return CommonPeopleIdNameListResponse(ErrorType.SQLReadError, mutableListOf())
    }
    return CommonPeopleIdNameListResponse(ErrorType.NoError, data)
  }
}
