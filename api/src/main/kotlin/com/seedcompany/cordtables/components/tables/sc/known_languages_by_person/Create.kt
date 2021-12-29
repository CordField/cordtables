package com.seedcompany.cordtables.components.tables.sc.known_languages_by_person

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.knownLanguagesByPersonInput
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.Read
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScKnownLanguagesByPersonCreateRequest(
    val token: String? = null,
    val knownLanguagesByPerson: knownLanguagesByPersonInput,
)

data class ScKnownLanguagesByPersonCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScKnownLanguagesByPersonCreate")
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

    @PostMapping("sc-known-languages-by-person/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScKnownLanguagesByPersonCreateRequest): ScKnownLanguagesByPersonCreateResponse {

        // if (req.knownLanguagesByPerson.name == null) return ScKnownLanguagesByPersonCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.known_languages_by_person(person, known_language,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
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
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.knownLanguagesByPerson.person,
            req.knownLanguagesByPerson.known_language,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return ScKnownLanguagesByPersonCreateResponse(error = ErrorType.NoError, id = id)
    }

}

