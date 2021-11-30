package com.seedcompany.cordtables.components.tables.sc.ethnologue

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ethnologueInput
import com.seedcompany.cordtables.components.tables.sc.ethnologue.Read
import com.seedcompany.cordtables.components.tables.sc.ethnologue.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScEthnologueCreateRequest(
    val token: String? = null,
    val ethnologue: ethnologueInput,
)

data class ScEthnologueCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScEthnologueCreate")
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

    @PostMapping("sc-ethnologue/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScEthnologueCreateRequest): ScEthnologueCreateResponse {

        // if (req.ethnologue.name == null) return ScEthnologueCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.ethnologue(neo4j_id, language_index, code, language_name, population, provisional_code, sensitivity, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?::common.sensitivity,
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
            req.ethnologue.neo4j_id,
            req.ethnologue.language_index,
            req.ethnologue.code,
            req.ethnologue.language_name,
            req.ethnologue.population,
            req.ethnologue.provisional_code,
            req.ethnologue.sensitivity,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScEthnologueCreateResponse(error = ErrorType.NoError, id = id)
    }

}