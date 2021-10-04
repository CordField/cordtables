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

data class GroupDeleteRequest(
    val token: String? = null,
    val id: Int? = null,
)

data class GroupDeleteResponse(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordfield.org", "https://cordfield.org"])
@Controller("GroupsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("groups/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: GroupDeleteRequest): GroupDeleteResponse {

        if (req.token == null) return GroupDeleteResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GroupDeleteResponse(ErrorType.AdminOnly)

        if (req.id == null) return GroupDeleteResponse(ErrorType.MissingId)

        this.ds.connection.use { conn ->

            //language=SQL
            val deleteStatement = conn.prepareStatement(
                """
                delete from public.groups where id = ?;
            """.trimIndent()
            )

            deleteStatement.setInt(1, req.id)

            deleteStatement.execute()
        }

        return GroupDeleteResponse(ErrorType.NoError)
    }

}