package com.seedcompany.cordtables.components.tables.admin.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class PeopleDeleteRequest(
    val token: String? = null,
    val id: Int? = null,
)

data class PeopleDeleteResponse(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("PeopleDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("people/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: PeopleDeleteRequest): PeopleDeleteResponse {

        if (req.token == null) return PeopleDeleteResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return PeopleDeleteResponse(ErrorType.AdminOnly)

        if (req.id == null) return PeopleDeleteResponse(ErrorType.MissingId)

        this.ds.connection.use { conn ->

            //language=SQL
            val deleteStatement = conn.prepareStatement(
                """
                delete from admin.people where id = ?;
            """.trimIndent()
            )

            deleteStatement.setInt(1, req.id)

            deleteStatement.execute()
        }

        return PeopleDeleteResponse(ErrorType.NoError)
    }

}