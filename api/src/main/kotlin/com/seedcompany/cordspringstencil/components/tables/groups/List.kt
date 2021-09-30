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

data class GroupsRow(
    val id: Int,
    val name: String,
    val createdAt: String,
    val createdBy: Int,
    val modifiedAt: String,
    val modifiedBy: Int,
)

data class GroupsListRequest(
    val token: String? = null,
)

data class GroupsListReturn(
    val error: ErrorType,
    val groups: List<out GroupsRow>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordfield.org", "https://cordfield.org"])
@Controller
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("groups/list")
    @ResponseBody
    fun listHandler(@RequestBody req: GroupsListRequest): GroupsListReturn {

        if (req.token == null) return GroupsListReturn(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return GroupsListReturn(ErrorType.AdminOnly, null)

        val items = mutableListOf<GroupsRow>()

        this.ds.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareStatement("""
                select * from groups;
            """.trimIndent())

            val result = statement.executeQuery()

            while (result.next()){
                items.add(
                    GroupsRow(
                        id = result.getInt("id"),
                        name = result.getString("name"),
                        createdAt = result.getString("created_at"),
                        createdBy = result.getInt("created_by"),
                        modifiedAt = result.getString("modified_at"),
                        modifiedBy = result.getInt("modified_by"),
                    )
                )
            }
        }

        return GroupsListReturn(ErrorType.NoError, items)
    }

}