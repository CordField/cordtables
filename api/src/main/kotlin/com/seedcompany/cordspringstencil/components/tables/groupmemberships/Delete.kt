package com.seedcompany.cordspringstencil.components.tables.groupmemberships

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class GroupRowAccessDeleteRequest(
    val token: String? = null,
    val id: Int? = null,
)

data class GroupRowAccessDeleteResponse(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordfield.org", "https://cordfield.org"])
@Controller("GroupRowAccessDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("grouprowaccess/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: GroupRowAccessDeleteRequest): GroupRowAccessDeleteResponse {

        if (req.token == null) return GroupRowAccessDeleteResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GroupRowAccessDeleteResponse(ErrorType.AdminOnly)

        if (req.id == null) return GroupRowAccessDeleteResponse(ErrorType.MissingId)

        this.ds.connection.use { conn ->

            //language=SQL
            val deleteStatement = conn.prepareStatement(
                """
                delete from public.group_row_access where id = ?;
            """.trimIndent()
            )

            deleteStatement.setInt(1, req.id)

            deleteStatement.execute()
        }

        return GroupRowAccessDeleteResponse(ErrorType.NoError)
    }

}