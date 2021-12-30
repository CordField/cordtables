package com.seedcompany.cordtables.components.tables.up.prayer_requests

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sil.language_index.languageIndex
import com.seedcompany.cordtables.components.user.Register
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
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
  val creatorEmail: String, // admin.people // admin.users // created_by, updated_by, owning_person
  val translatorEmail: String?, // admin.people // admin.users // translator
  val ethCode: String?, // sil.language_index lang field value
  val sensitivity: String?, // CommonSensitivity = CommonSensitivity.High,
  val location: String?,
  val prayerType: String,
  val title: String,
  val content: String,
)

data class UpPrayerRequestsCreateFromFormRequest(
  val token: String? = null,
  val prayerForm: PrayerForm,
)

data class UpPrayerRequestsCreateFromFormResponse(
  val error: ErrorType,
  val id: String? = null,
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

  @Autowired
  val reg: Register
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
    var creatorUserId: Int?
    var translatorUserId: Int?

    creatorUserId = checkUserExists(req.prayerForm.creatorEmail)
    translatorUserId = req.prayerForm.translatorEmail?.let { checkUserExists(it) }

    var langExists = req.prayerForm.ethCode?.let { getSilLanguageData(it) }

    if(creatorUserId == 0){
      val pass = util.encoder.encode("somepassword")
      val tkn = util.createToken()
      reg.registerDB(req.prayerForm.creatorEmail, pass, tkn)
      creatorUserId = checkUserExists(req.prayerForm.creatorEmail)
    }

    if(translatorUserId == 0){
      val pass = util.encoder.encode("somepassword")
      val tkn = util.createToken()
      req.prayerForm.translatorEmail?.let { reg.registerDB(it, pass, tkn) }
      translatorUserId = req.prayerForm.translatorEmail?.let { checkUserExists(it) }
    }

    val id = jdbcTemplate.queryForObject(
      """
            insert into up.prayer_requests(request_language_id, sensitivity, translator, location, title, content, reviewed, prayer_type, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::common.sensitivity,
                    ?::uuid,
                    ?::uuid,
                    ?,
                    ?,
                    false,
                    ?::up.prayer_type,
                    ?,
                    ?,
                    ?,
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
      String::class.java,
      langExists!!.common_id,
      req.prayerForm.sensitivity,
      translatorUserId,
      req.prayerForm.location,
      req.prayerForm.title,
      req.prayerForm.content,
      req.prayerForm.prayerType,
      creatorUserId,
      creatorUserId,
      creatorUserId,
      util.adminGroupId
    )
    return UpPrayerRequestsCreateFromFormResponse(error = ErrorType.NoError, id = id)
  }

  fun getSilLanguageData(ethCode: String): languageIndex {
    var data = jdbcTemplate.query(
      """
        SELECT * FROM sil.language_index WHERE lang='${ethCode}'
      """.trimIndent()
    ){ rs, rowNum ->
      languageIndex(
        rs.getString("id"),
        rs.getString("common_id"),
        rs.getString("lang"),
        rs.getString("country"),
        rs.getString("name_type"),
        rs.getString("name"),
      )
    }
    return data.first()
  }

  fun checkDataExists(table: String, field: String, fieldValue: String): Int {
    var id: Int?
    id = jdbcTemplate.queryForObject(
      """
          SELECT id FROM $table WHERE $field = ?;
      """.trimIndent(),
      Int::class.java,
      fieldValue,
    )
    return  id;
  }


  fun checkUserExists(email: String):Int{
    return try {
      jdbcTemplate.queryForObject(
        """
            SELECT id FROM admin.users WHERE email = ?;
          """.trimIndent(),
        Int::class.java,
        email
        )
    }
    catch (e: EmptyResultDataAccessException){
      0
    }
  }

}
