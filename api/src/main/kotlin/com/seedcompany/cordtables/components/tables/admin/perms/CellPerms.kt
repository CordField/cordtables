package com.seedcompany.cordtables.components.tables.admin.perms

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import org.springframework.beans.factory.annotation.Autowired
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
  val secureList: GetSecureListQuery
) {
  @PostMapping("admin/perms/cell")
  @ResponseBody
  fun handler(@RequestBody req: CellPermsRequest): CellPermsResponse{
    return CellPermsResponse(ErrorType.NoError, null)
  }
}
