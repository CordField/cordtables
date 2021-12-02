package com.seedcompany.cordtables.components.tables.up.prayer_requests

import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.prayer_requests.Read
import com.seedcompany.cordtables.components.tables.common.prayer_requests.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

enum class UpPrayerTypes{
  Request,
  Update,
  Celebration,
}

data class PrayerForm (
  val creatorEmail: String,
  val translatorEmail: String?,
  val ethCode: String?,
  val sensitivity: CommonSensitivity = CommonSensitivity.High,
  val location: String?,
  val prayerType: UpPrayerTypes,
  val title: String,
  val content: String,
)

data class UpPrayerRequestsCreateFromFormRequest(
  val token: String? = null,
  val prayerForm: PrayerForm,
)

data class UpPrayerRequestsCreateFromFormResponse(
  val error: ErrorType,
  val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerRequestsCreateFromForm")
class CreateFromGoogleForm(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

  @Autowired
  val update: Update,

  @Autowired
  val read: Read,
) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  /**
   * This endpoint is an abstraction that exists for the prayer request google form
   * that is maintained by someone else. It feeds the prayer requests for Unceasing Prayer.
   *
   * The Google Form will send the PrayerForm payload shown above.
   *
   * The new prayer schema is up.prayer_requests
   *
   * creatorEmail - create a person/user if they don't exist, use the person id as the creator
   * translatorEmail - create a person/user if they don't exist, use as the translator person id
   * ethCode - ensure an etry exists in sil.language_index, get the common.languages(id) from that
   * sensitivity - the form should pass in 'Low' or 'Medium'
   * location - save directly to column
   * title - save directly to column
   * content - save directly to column
   *
   */

  @PostMapping("up-prayer-requests/create-from-form")
  @ResponseBody
  fun createHandler(@RequestBody req: UpPrayerRequestsCreateFromFormRequest): UpPrayerRequestsCreateFromFormResponse {



    return UpPrayerRequestsCreateFromFormResponse(error = ErrorType.NoError, id = 42)
  }

}