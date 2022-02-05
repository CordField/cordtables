package com.seedcompany.cordtables.components.admin.perms

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CellPermsRequest(
  val token: String,
  val tableName: String,
  val columnName: String,
  val rowId: String,
)

data class CellPermsResponse(
  val error: ErrorType,
  val perm: String?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminCellPerms")
class CellPerms(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val secureList: GetSecureListQuery,

  @Autowired
  val rowPerms: RowPerms
) {
  @PostMapping("admin/perms/cell")
  @ResponseBody
  fun handler(@RequestBody req: CellPermsRequest): CellPermsResponse{
    if(req.token === null) return CellPermsResponse(ErrorType.TokenNotFound,null)
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
    val columnPermsForRowId = rowPerms.handler(RowPermsRequest(token = req.token, id = req.rowId, table = req.tableName))
    val columnPermArray = columnPermsForRowId.perms?.filter{it->it.columnName==req.columnName}
    println(columnPermArray)
    println(columnPermsForRowId.perms)
    if(columnPermArray=== null || columnPermArray.isEmpty()) {
    return CellPermsResponse(ErrorType.IncorrectColumnName, null)
    }
    else{
      val columnPerm = columnPermArray.get(0).perm
      return CellPermsResponse(ErrorType.NoError, columnPerm)
    }
//      .get(0)?.perm
  }
}
