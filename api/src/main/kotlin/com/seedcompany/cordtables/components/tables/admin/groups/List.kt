package com.seedcompany.cordtables.components.tables.admin.groups

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import org.springframework.beans.factory.annotation.Autowired
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


data class GroupsRow(
    val id: Int?,
    val name: String?,
    val createdAt: String?,
    val createdBy: Int?,
    val modifiedAt: String?,
    val modifiedBy: Int?,
    val owningPerson: Int?,
    val owningGroup: Int?,
)

data class GroupsListRequest(
    val token: String? = null,
)

data class GroupsListReturn(
    val error: ErrorType,
    val groups: List<out GroupsRow>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GroupsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("groups/list")
    @ResponseBody
    fun listHandler(@RequestBody req: GroupsListRequest): GroupsListReturn {

        if (req.token == null) return GroupsListReturn(ErrorType.TokenNotFound, null)
        if (!util.isAdmin(req.token)) return GroupsListReturn(ErrorType.AdminOnly, null)

        val items = mutableListOf<GroupsRow>()

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.groups",
                columns = arrayOf(
                    "id",
                    "name",
                    "createdAt",
                    "createdBy",
                    "modifiedAt",
                    "modifiedBy",
                    "owningPerson",
                    "owningGroup"
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var name: String? = jdbcResult.getString("name")
                if (jdbcResult.wasNull()) name = null

                var createdAt: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) createdAt = null

                var createdBy: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) createdBy = null

                var modifiedAt: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modifiedAt = null

                var modifiedBy: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modifiedBy = null

                var owningPerson: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owningPerson = null

                var owningGroup: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owningGroup = null

                items.add(
                    GroupsRow(
                        id = id,
                        name = name,
                        createdAt = createdAt,
                        createdBy = createdBy,
                        modifiedAt = modifiedAt,
                        modifiedBy = modifiedBy,
                        owningPerson = owningPerson,
                        owningGroup = owningGroup,
                )
            )
        }
        } catch (e: SQLException) {
            println("error while listting ${e.message}")
            return GroupsListReturn(ErrorType.SQLReadError, mutableListOf())
        }

        return GroupsListReturn(ErrorType.NoError, items)
    }

}