package com.seedcompany.cordtables.components.tables.admin.perms

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleReadRequest
import com.seedcompany.cordtables.components.tables.admin.people.AdminPeopleReadResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
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
@Controller("AdminRowPerms")
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


    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
//    val paramSource = MapSqlParameterSource()
//    paramSource.addValue("token", req.token)
//
//    paramSource.addValue("table", req.table)
//    paramSource.addValue("column", req.column)
    //language=SQL
    this.ds.connection.use{ conn ->
      val statement = conn.prepareCall("""
        select access_level 
        from admin.role_column_grants rcg 
        inner join admin.roles r 
        on r.id = rcg.role  
        inner join admin.role_memberships rm 
        on r.id = rm.role 
        inner join admin.tokens t 
        on rm.person = t.person 
        where rcg.column_name = ? 
        and rcg.table_name = ?::admin.table_name 
        and t.token = ?
      """.replace('\n', ' '))
//      val statement2 = conn.prepareCall("""
//        select group_id, person
//        from admin.organization_administrators
//        where
//      """.replace('\n', ' '))
      statement.setString(1,req.id)
      statement.setString(2,req.table)
      statement.setString(3,req.token)
      try {
//        val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)

//        val jdbcResult = statement.executeQuery()
//        var perm:String? = null
//        while(jdbcResult.next()) {
//          var accessLevel:String? = jdbcResult.getString("access_level")
//          if(jdbcResult.wasNull()) accessLevel = null
//          if(perm === null && accessLevel !== null){
//            perm = accessLevel
//          }
//          println(perm)
//          if(accessLevel === "Write"){
//            perm = accessLevel
//            break;
//          }
//        }
//        return RowPermsResponse(ErrorType.NoError, perm)
      }
      catch(e: SQLException){
        println("error while listing ${e.message}")
        return RowPermsResponse(ErrorType.SQLReadError, null)
      }
    }

    return RowPermsResponse(ErrorType.NoError)
  }
}
