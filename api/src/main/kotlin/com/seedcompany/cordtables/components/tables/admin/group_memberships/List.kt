package com.seedcompany.cordtables.components.tables.admin.group_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource
import kotlin.collections.List

data class GroupMembershipsRow(
    val id: Int?,
    val group: Int?,
    val person: Int?,
    val createdAt: String?,
    val createdBy: Int?,
    val modifiedAt: String?,
    val modifiedBy: Int?,
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

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("group_memberships/list")
    @ResponseBody
    fun listHandler(@RequestBody req: GroupMembershipsListRequest): GroupMembershipsListReturn {

        var items = mutableListOf<GroupMembershipsRow>()
        if (req.token == null) return GroupMembershipsListReturn(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return GroupMembershipsListReturn(ErrorType.AdminOnly, null)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.group_memberships",
                columns = arrayOf(
                    "id",
                    "group_id",
                    "person",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by"
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {
                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var group: Int? = jdbcResult.getInt("group_id")
                if (jdbcResult.wasNull()) group = null

                var person: Int? = jdbcResult.getInt("person")
                if (jdbcResult.wasNull()) person = null

                var createdAt: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) createdAt = null

                var createdBy: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) createdBy = null

                var modifiedAt: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modifiedAt = null

                var modifiedBy: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modifiedBy = null

                items.add(
                    GroupMembershipsRow(
                        id = id,
                        group = group,
                        person = person,
                        createdAt = createdAt,
                        createdBy = createdBy,
                        modifiedAt = modifiedAt,
                        modifiedBy = modifiedBy

                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return GroupMembershipsListReturn(ErrorType.SQLReadError, mutableListOf())
        }



//        this.ds.connection.use { conn ->
//            //language=SQL
//            val statement = conn.prepareStatement("""
//                select * from admin.group_memberships order by id asc;
//            """.trimIndent())
//
//            val result = statement.executeQuery()
//
//            while (result.next()){
//                items.add(
//                    GroupMembershipsRow(
//                        id = result.getInt("id"),
//                        group = result.getInt("group_id"),
//                        person = result.getInt("person"),
//                        createdAt = result.getString("created_at"),
//                        createdBy = result.getInt("created_by"),
//                        modifiedAt = result.getString("modified_at"),
//                        modifiedBy = result.getInt("modified_by"),
//                    )
//                )
//            }
//        }

        return GroupMembershipsListReturn(ErrorType.NoError, items)
    }

}