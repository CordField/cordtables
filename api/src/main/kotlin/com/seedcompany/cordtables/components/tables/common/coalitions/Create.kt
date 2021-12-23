package com.seedcompany.cordtables.components.tables.common.coalitions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.coalitions.coalitionInput
import com.seedcompany.cordtables.components.tables.common.coalitions.Read
import com.seedcompany.cordtables.components.tables.common.coalitions.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCoalitionsCreateRequest(
    val token: String? = null,
    val coalition: coalitionInput,
)

data class CommonCoalitionsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCoalitionsCreate")
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

    @PostMapping("common-coalitions/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonCoalitionsCreateRequest): CommonCoalitionsCreateResponse {

        if (req.coalition.name == null) return CommonCoalitionsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.coalitions(name, created_by, modified_by, owning_person, owning_group)
                values(
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
            req.coalition.name,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return CommonCoalitionsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
