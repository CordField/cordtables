package com.seedcompany.cordspringstencil.components.tables.groups

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class GroupCreateRequest(
    val token: String? = null,
    val name: String? = null,
)

data class GroupCreateReturn(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordfield.org", "https://cordfield.org"])
@Controller
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("groups/create")
    @ResponseBody
    fun createHandler(@RequestBody req: GroupUpdateRequest): GroupUpdateResponse {

        if (req.token == null) return GroupUpdateResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GroupUpdateResponse(ErrorType.AdminOnly)

        if (req.name == null) return GroupUpdateResponse(ErrorType.InputMissingName)
        if (req.name.isEmpty()) return GroupUpdateResponse(ErrorType.NameTooShort)
        if (req.name.length > 64) return GroupUpdateResponse(ErrorType.NameTooLong)

        this.ds.connection.use { conn ->

            //language=SQL
            val checkNameStatement = conn.prepareStatement(
                """
                select exists(select id from public.groups where name = ?)
            """.trimIndent()
            )

            checkNameStatement.setString(1, req.name)

            val result = checkNameStatement.executeQuery();

            if (result.next()) {
                val nameFound = result.getBoolean(1)

                if (nameFound) {
                    return GroupUpdateResponse(ErrorType.NameAlreadyExists)
                } else {

                    //language=SQL
                    val statement = conn.prepareStatement(
                        """
                        insert into public.groups(name, created_by, modified_by) 
                            values(?, 
                                (
                                  select person 
                                  from public.tokens 
                                  where token = ?
                                ),
                                (
                                  select person 
                                  from public.tokens 
                                  where token = ?
                                )
                        );
                        """.trimIndent()
                                )

                    statement.setString(1, req.name)
                    statement.setString(2, req.token)
                    statement.setString(3, req.token)

                    statement.execute()

                }
            }


        }

        return GroupUpdateResponse(ErrorType.NoError)
    }

}