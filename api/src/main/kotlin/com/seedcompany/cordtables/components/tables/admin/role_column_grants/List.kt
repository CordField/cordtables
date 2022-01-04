package com.seedcompany.cordtables.components.tables.admin.role_column_grants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.admin.role_column_grants.roleColumnGrant
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


data class AdminRoleColumnGrantsListRequest(
    val token: String?
)

data class AdminRoleColumnGrantsListResponse(
    val error: ErrorType,
    val roleColumnGrants: MutableList<roleColumnGrant>? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleColumnGrantsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("admin/role-column-grants/list")
    @ResponseBody
    fun listHandler(@RequestBody req:AdminRoleColumnGrantsListRequest): AdminRoleColumnGrantsListResponse {

      if (req.token == null) return AdminRoleColumnGrantsListResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminRoleColumnGrantsListResponse(ErrorType.AdminOnly)

        var data: MutableList<roleColumnGrant> = mutableListOf()
        if (req.token == null) return AdminRoleColumnGrantsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.role_column_grants",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "role",
                    "table_name",
                    "column_name",
                    "access_level",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var role: String? = jdbcResult.getString("role")
                if (jdbcResult.wasNull()) role = null

                var table_name: String? = jdbcResult.getString("table_name")
                if (jdbcResult.wasNull()) table_name = null

                var column_name: String? = jdbcResult.getString("column_name")
                if (jdbcResult.wasNull()) column_name = null

                var access_level: String? = jdbcResult.getString("access_level")
                if (jdbcResult.wasNull()) access_level = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
                    roleColumnGrant(
                        id = id,
                        role = role,
                        table_name = table_name, // if (table_name == null) null else TableNames.valueOf(table_name),
                        column_name = column_name,
                        access_level = access_level, //if (access_level == null) null else AccessLevels.valueOf(access_level),
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return AdminRoleColumnGrantsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return AdminRoleColumnGrantsListResponse(ErrorType.NoError, data)
    }
}
