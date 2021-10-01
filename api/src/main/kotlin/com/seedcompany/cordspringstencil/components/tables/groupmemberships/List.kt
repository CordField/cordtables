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
import kotlin.collections.List

data class GroupRowAccessRow(
    val id: Int,
    val group: Int,
    val tableName: String,
    val row: Int,
    val createdAt: String,
    val createdBy: Int,
    val modifiedAt: String,
    val modifiedBy: Int,
)

data class GroupRowAccessListRequest(
    val token: String? = null,
)

data class GroupRowAccessListReturn(
    val error: ErrorType,
    val groupRowAccessList: List<out GroupRowAccessRow>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordfield.org", "https://cordfield.org"])
@Controller("GroupRowAccessList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("grouprowaccess/list")
    @ResponseBody
    fun listHandler(@RequestBody req: GroupRowAccessListRequest): GroupRowAccessListReturn {

        if (req.token == null) return GroupRowAccessListReturn(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return GroupRowAccessListReturn(ErrorType.AdminOnly, null)

        val items = mutableListOf<GroupRowAccessRow>()

        this.ds.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareStatement("""
                select * from public.group_row_access order by id asc;
            """.trimIndent())

            val result = statement.executeQuery()

            while (result.next()){
                items.add(
                    GroupRowAccessRow(
                        id = result.getInt("id"),
                        group = result.getInt("group_id"),
                        tableName = result.getString("table_name"),
                        row = result.getInt("row"),
                        createdAt = result.getString("created_at"),
                        createdBy = result.getInt("created_by"),
                        modifiedAt = result.getString("modified_at"),
                        modifiedBy = result.getInt("modified_by"),
                    )
                )
            }
        }

        return GroupRowAccessListReturn(ErrorType.NoError, items)
    }

}