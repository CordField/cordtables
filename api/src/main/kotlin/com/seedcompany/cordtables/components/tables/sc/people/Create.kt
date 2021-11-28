package com.seedcompany.cordtables.components.tables.sc.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.people.peopleInput
import com.seedcompany.cordtables.components.tables.sc.people.Read
import com.seedcompany.cordtables.components.tables.sc.people.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPeopleCreateRequest(
    val token: String? = null,
    val people: peopleInput,
)

data class ScPeopleCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScPeopleCreate")
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

    @PostMapping("sc-people/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScPeopleCreateRequest): ScPeopleCreateResponse {

        // if (req.people.name == null) return ScPeopleCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.people(neo4j_id, skills, status, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
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
            Int::class.java,
            req.people.neo4j_id,
            req.people.skills,
            req.people.status,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScPeopleCreateResponse(error = ErrorType.NoError, id = id)
    }

}