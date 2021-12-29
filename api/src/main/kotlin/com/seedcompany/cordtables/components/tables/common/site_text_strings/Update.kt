package com.seedcompany.cordtables.components.tables.common.site_text_strings

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class SiteTextStringUpdateInput(
  val id: Int,
  val column: String,
  val newValue: String?
)

data class SiteTextStringUpdateRequest(
  val token: String,
  val site_text_string: SiteTextStringUpdateInput,
)

data class SiteTextStringUpdateResponse(
  val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextStringUpdate")
class Update(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,
) {
  @PostMapping("common/site-text-strings/update")
  @ResponseBody
  fun updateHandler(@RequestBody req: SiteTextStringUpdateRequest): SiteTextStringUpdateResponse {

    if (req.token == null) return SiteTextStringUpdateResponse(ErrorType.TokenNotFound)
    if (req.site_text_string.id == null) return SiteTextStringUpdateResponse(ErrorType.MissingId)

    if (req.site_text_string.column != null) util.updateField(
      token = req.token,
      table = "common.site_text_strings",
      column = req.site_text_string.column,
      id = req.site_text_string.id,
      value = req.site_text_string.newValue,
    )
    return SiteTextStringUpdateResponse(ErrorType.NoError)
  }
}
