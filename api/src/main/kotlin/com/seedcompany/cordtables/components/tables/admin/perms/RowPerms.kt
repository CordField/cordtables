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
  val table: String,
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
               val secureList: GetSecureListQuery,

               @Autowired
               val columnPerms: ColumnPerms
) {
  var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

  @PostMapping("admin/perms/row")
  @ResponseBody
  fun handler(@RequestBody req: RowPermsRequest): RowPermsResponse {
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
    this.ds.connection.use { conn ->
      val tableName = req.table.split('.')[1]
      val schemaName = req.table.split('.')[0]
      val statement1 = conn.prepareCall("""
        select column_name from 
        information_schema.columns 
        where table_name = ? 
        and table_schema = ?
      """.replace('\n', ' '))

      statement1.setString(0, tableName)
      statement1.setString(1, schemaName)

      // now call the column perms for each column and call row id


      try {
        var data: MutableList<RowPermsData> = mutableListOf();

        val jdbcResult = statement1.executeQuery()
        while (jdbcResult.next()) {
          var columnName: String = jdbcResult.getString("column_name")
          val columnPerm = columnPerms.handler(ColumnPermsRequest(token = req.token, table = req.table, column = columnName))

          if(columnPerm.rowAccessRequired) {

            val statement2 = conn.prepareCall("""
              select exists(select id 
              from admin.group_row_access gra 
              inner join admin.groups g 
              on g.id = gra.group_id  
              inner join admin.group_memberships gm 
              on g.id = gm.group_id 
              inner join admin.tokens t 
              on gm.person = t.person 
              where gra.row = ? 
              and gra.table_name = ?::admin.table_name 
              and t.token = ?)
              """.replace('\n', ' '))

            statement2.setString(1, req.id)
            statement2.setString(2, req.table)
            statement2.setString(3, req.token)
            val result = statement2.executeQuery()
            if(result.next()) {
             data.add(RowPermsData(columnName = columnName, perm = columnPerm.perm))
            }else{
              data.add(RowPermsData(columnName=columnName, perm = null))
            }
          }
          else{
             data.add(RowPermsData(columnName = columnName, perm = columnPerm.perm))
          }
        }

      } catch (e: SQLException) {
        println("error while listing ${e.message}")
        return RowPermsResponse(ErrorType.SQLReadError, null)
      }
    }

    return RowPermsResponse(ErrorType.NoError)
  }
}
