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
import kotlin.collections.List

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
    fun listHandler(@RequestBody req: GroupCreateRequest): GroupCreateReturn {

        if (req.token == null) return GroupCreateReturn(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GroupCreateReturn(ErrorType.AdminOnly)

        if (req.name == null) return GroupCreateReturn(ErrorType.InputMissingName)
        if (req.name.isEmpty()) return GroupCreateReturn(ErrorType.NameTooShort)
        if (req.name.length > 64) return GroupCreateReturn(ErrorType.NameTooLong)

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
                    return GroupCreateReturn(ErrorType.NameAlreadyExists)
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

        return GroupCreateReturn(ErrorType.NoError)
    }

}