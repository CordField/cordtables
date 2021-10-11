package com.seedcompany.cordtables.components.tables.grouprowaccess

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource
import kotlin.collections.List

data class GroupMembershipsRow(
    val id: Int,
    val group: Int,
    val person: Int,
    val createdAt: String,
    val createdBy: Int,
    val modifiedAt: String,
    val modifiedBy: Int,
)

data class GroupMembershipsListRequest(
    val token: String? = null,
)

data class GroupMembershipsListReturn(
    val error: ErrorType,
    val groupMemberships: List<out GroupMembershipsRow>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GroupMembershipsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("groupmemberships/list")
    @ResponseBody
    fun listHandler(@RequestBody req: GroupMembershipsListRequest): GroupMembershipsListReturn {

        if (req.token == null) return GroupMembershipsListReturn(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return GroupMembershipsListReturn(ErrorType.AdminOnly, null)

        val items = mutableListOf<GroupMembershipsRow>()

        this.ds.connection.use { conn ->
            //language=SQL
            val statement = conn.prepareStatement("""
                select * from admin.group_memberships order by id asc;
            """.trimIndent())

            val result = statement.executeQuery()

            while (result.next()){
                items.add(
                    GroupMembershipsRow(
                        id = result.getInt("id"),
                        group = result.getInt("group_id"),
                        person = result.getInt("person"),
                        createdAt = result.getString("created_at"),
                        createdBy = result.getInt("created_by"),
                        modifiedAt = result.getString("modified_at"),
                        modifiedBy = result.getInt("modified_by"),
                    )
                )
            }
        }

        return GroupMembershipsListReturn(ErrorType.NoError, items)
    }

}