package com.seedcompany.cordtables.components.tables.admin.perms

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleReadRequest
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleReadResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class RowPermsData(
  val columnName: String,
  val perm: String?
)
data class RowPermsRequest(
  val token: String,
  val id: String,
  val table:String,
)
data class RowPermsResponse(
  val error: ErrorType,
  val perms: MutableList<RowPermsData>? = null
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminPeopleRead")
class RowPerms(@Autowired
               val util: Utility,

               @Autowired
               val ds: DataSource,

               @Autowired
               val secureList: GetSecureListQuery) {
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("admin/perms/row")
  @ResponseBody
  fun handler(@RequestBody req: RowPermsRequest): RowPermsResponse{
    if (req.token == null) return RowPermsResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return RowPermsResponse(ErrorType.AdminOnly)

    if (req.id == null) return RowPermsResponse(ErrorType.MissingId)
    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", req.token)
    paramSource.addValue("id", req.id)

    return RowPermsResponse(ErrorType.NoError)
  }
}
