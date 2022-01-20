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

enum class FakeDataControlRequestType {
  LoadCommonFakeData,
}

data class FakeDataControlRequest(
  val token: String,
  val type: FakeDataControlRequestType,
  val version: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("FakeDataControl")
class FakeDataControl(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val appConfig: AppConfig,

  @Autowired
  val vc: FakeDataCreatorControl,
) {

  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  @PostMapping("fake-data/control")
  @ResponseBody
  fun createHandler(@RequestBody req: FakeDataControlRequest): GenericResponse {
    if (!util.isAdmin(req.token)) return GenericResponse(error = ErrorType.AdminOnly)

    when (req.type){
      FakeDataControlRequestType.LoadCommonFakeData -> {
        vc.loadCommonFakeData()
      }
    }

    return GenericResponse(error = ErrorType.NoError)
  }
}
