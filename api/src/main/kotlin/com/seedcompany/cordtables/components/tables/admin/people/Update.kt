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

data class PeopleUpdateRequest(
    val token: String? = null,
    val id: Int? = null,
    val publicFullName: String? = null
)

data class PeopleUpdateResponse(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("PeopleUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("people/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: PeopleUpdateRequest): PeopleUpdateResponse {

        if (req.token == null) return PeopleUpdateResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return PeopleUpdateResponse(ErrorType.AdminOnly)
        if (req.id === 1) return PeopleUpdateResponse(ErrorType.CannotUpdateAdminGroup)

        if (req.publicFullName == null) return PeopleUpdateResponse(ErrorType.InputMissingName)
        if (req.publicFullName.isEmpty()) return PeopleUpdateResponse(ErrorType.NameTooShort)
        if (req.publicFullName.length > 64) return PeopleUpdateResponse(ErrorType.NameTooLong)

        if (req.id == null) return PeopleUpdateResponse(ErrorType.MissingId)

        this.ds.connection.use { conn ->

            //language=SQL
            val checkNameStatement = conn.prepareStatement(
                """
                select exists(select id from admin.people where public_full_name = ?)
            """.trimIndent()
            )

            checkNameStatement.setString(1, req.publicFullName)

            val result = checkNameStatement.executeQuery();

            if (result.next()) {
                val nameFound = result.getBoolean(1)

                if (nameFound) {
                    return PeopleUpdateResponse(ErrorType.NameAlreadyExists)
                } else {

                    //language=SQL
                    val statement = conn.prepareStatement(
                        """
                        update admin.people
                        set 
                          public_full_name = ?, 
                          modified_by = (
                                  select person 
                                  from admin.tokens 
                                  where token = ?
                                ), 
                          modified_at = CURRENT_TIMESTAMP
                        where id = ?;
                        """.trimIndent()
                    )

                    statement.setString(1, req.publicFullName)
                    statement.setString(2, req.token)
                    statement.setInt(3, req.id)

                    statement.execute()

                }
            }

        }

        return PeopleUpdateResponse(ErrorType.NoError)
    }

}