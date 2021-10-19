package com.seedcompany.cordtables.components.tables.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class PeopleCreateRequest(
    val token: String? = null
)

data class PeopleCreateResponse(
    val error: ErrorType
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("PeopleCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("people/create")
    @ResponseBody
    fun createHandler(@RequestBody req: PeopleCreateRequest): PeopleCreateResponse {

        if (req.token == null) return PeopleCreateResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return PeopleCreateResponse(ErrorType.AdminOnly)

        this.ds.connection.use { conn ->

            val statement = conn.prepareStatement(
                """
                    insert into admin.people(created_by, modified_by)
                        values(
                        (
                         select person
                         from admin.tokens
                         where token = ?
                        ),
                        (
                         select person 
                         from admin.tokens
                         where token = ?
                        )
                    );
                """.trimIndent()
            )

            statement.setString(1, req.token)
            statement.setString(2, req.token)

            statement.execute()
        }
        return PeopleCreateResponse(ErrorType.NoError)
    }
}



