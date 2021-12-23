package com.seedcompany.cordtables.components.tables.sc.person_unavailabilities

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.personUnavailabilityInput
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.Read
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPersonUnavailabilitiesCreateRequest(
    val token: String? = null,
    val personUnavailability: personUnavailabilityInput,
)

data class ScPersonUnavailabilitiesCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPersonUnavailabilitiesCreate")
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

    @PostMapping("sc-person-unavailabilities/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScPersonUnavailabilitiesCreateRequest): ScPersonUnavailabilitiesCreateResponse {

        // if (req.personUnavailability.name == null) return ScPersonUnavailabilitiesCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.person_unavailabilities(person, period_start, period_end, description, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::timestamp,
                    ?::timestamp,
                    ?,
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
            String::class.java,
            req.personUnavailability.person,
            req.personUnavailability.period_start,
            req.personUnavailability.period_end,
            req.personUnavailability.description,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScPersonUnavailabilitiesCreateResponse(error = ErrorType.NoError, id = id)
    }

}
