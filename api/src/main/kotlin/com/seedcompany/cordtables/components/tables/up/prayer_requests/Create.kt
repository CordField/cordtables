package com.seedcompany.cordtables.components.tables.up.prayer_requests

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.up.prayer_requests.prayerRequestInput
import com.seedcompany.cordtables.components.tables.up.prayer_requests.Read
import com.seedcompany.cordtables.components.tables.up.prayer_requests.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class UpPrayerRequestsCreateRequest(
    val token: String? = null,
    val prayerRequest: prayerRequestInput,
)

data class UpPrayerRequestsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("UpPrayerRequestsCreate")
class Create(
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

    @PostMapping("up-prayer-requests/create")
    @ResponseBody
    fun createHandler(@RequestBody req: UpPrayerRequestsCreateRequest): UpPrayerRequestsCreateResponse {

        // if (req.prayerRequest.name == null) return UpPrayerRequestsCreateResponse(error = ErrorType.InputMissingToken, null)
        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into up.prayer_requests(language_id, sensitivity, parent, translator, location, title, content, reviewed, prayer_type, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::common.sensitivity,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?::boolean,
                    ?::up.prayer_type,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.prayerRequest.language_id,
            req.prayerRequest.sensitivity,
            req.prayerRequest.parent,
            req.prayerRequest.translator,
            req.prayerRequest.location,
            req.prayerRequest.title,
            req.prayerRequest.content,
            req.prayerRequest.reviewed,
            req.token,
            req.token,
            req.token,
        )


//        req.language.id = id

        return UpPrayerRequestsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
