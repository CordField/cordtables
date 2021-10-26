package com.seedcompany.cordtables.components.tables.admin.group_row_access

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
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

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GroupRowAccessDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("group_row_access/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: GroupRowAccessDeleteRequest): GroupRowAccessDeleteResponse {

        if (req.token == null) return GroupRowAccessDeleteResponse(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GroupRowAccessDeleteResponse(ErrorType.AdminOnly)

        if (req.id == null) return GroupRowAccessDeleteResponse(ErrorType.MissingId)

        this.ds.connection.use { conn ->

            //language=SQL
            val deleteStatement = conn.prepareStatement(
                """
                delete from admin.group_row_access where id = ?;
            """.trimIndent()
            )

            deleteStatement.setInt(1, req.id)

            deleteStatement.execute()
        }

        return GroupRowAccessDeleteResponse(ErrorType.NoError)
    }

}