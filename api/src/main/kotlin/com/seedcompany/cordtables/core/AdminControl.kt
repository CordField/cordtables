package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.GenericResponse
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

enum class AdminControlRequestType {
  LoadSilData,
  UpdateDbToVersion2,
}

data class AdminControlRequest(
  val token: String,
  val type: AdminControlRequestType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminControl")
class AdminControl(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val appConfig: AppConfig,

  @Autowired
  val vc: DatabaseVersionControl,
) {

  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  @PostMapping("admin/control")
  @ResponseBody
  fun createHandler(@RequestBody req: AdminControlRequest): GenericResponse {
    if (!util.isAdmin(req.token)) return GenericResponse(error = ErrorType.AdminOnly)

    when (req.type){
      AdminControlRequestType.LoadSilData -> {
        vc.loadSilData()
      }
      AdminControlRequestType.UpdateDbToVersion2 -> {
        vc.migrateToVersion2()
      }
    }

    return GenericResponse(error = ErrorType.NoError)
  }
}
