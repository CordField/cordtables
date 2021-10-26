package com.seedcompany.cordtables.components.tables.admin.group_row_access

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource
import kotlin.collections.List

data class GroupRowAccessRow(
    val id: Int?,
    val group: Int?,
    val tableName: String?,
    val row: Int?,
    val createdAt: String?,
    val createdBy: Int?,
    val modifiedAt: String?,
    val modifiedBy: Int?,
)

data class GroupRowAccessListRequest(
    val token: String? = null,
)

data class GroupRowAccessListReturn(
    val error: ErrorType,
    val groupRowAccessList: List<out GroupRowAccessRow>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GroupRowAccessList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("group_row_access/list")
    @ResponseBody
    fun listHandler(@RequestBody req: GroupRowAccessListRequest): GroupRowAccessListReturn {
        var items = mutableListOf<GroupRowAccessRow>()
        if (req.token == null) return GroupRowAccessListReturn(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return GroupRowAccessListReturn(ErrorType.AdminOnly, null)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.group_row_access",
                columns = arrayOf(
                    "id",
                    "group_id",
                    "table_name",
                    "row",
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

                var tableName: String? = jdbcResult.getString("table_name")
                if (jdbcResult.wasNull()) tableName = null

                var row: Int? = jdbcResult.getInt("row")
                if (jdbcResult.wasNull()) row = null

                var createdAt: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) createdAt = null

                var createdBy: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) createdBy = null

                var modifiedAt: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modifiedAt = null

                var modifiedBy: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modifiedBy = null

                items.add(
                    GroupRowAccessRow(
                        id = id,
                        group = group,
                        tableName = tableName,
                        row = row,
                        createdAt = createdAt,
                        createdBy = createdBy,
                        modifiedAt = modifiedAt,
                        modifiedBy = modifiedBy

                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return GroupRowAccessListReturn(ErrorType.SQLReadError, mutableListOf())
        }

//        this.ds.connection.use { conn ->
//            //language=SQL
//            val statement = conn.prepareStatement("""
//                select * from admin.group_row_access order by id asc;
//            """.trimIndent())
//
//            val result = statement.executeQuery()
//
//            while (result.next()){
//                items.add(
//                    GroupRowAccessRow(
//                        id = result.getInt("id"),
//                        group = result.getInt("group_id"),
//                        tableName = result.getString("table_name"),
//                        row = result.getInt("row"),
//                        createdAt = result.getString("created_at"),
//                        createdBy = result.getInt("created_by"),
//                        modifiedAt = result.getString("modified_at"),
//                        modifiedBy = result.getInt("modified_by"),
//                    )
//                )
//            }
//        }

        return GroupRowAccessListReturn(ErrorType.NoError, items)
    }

}