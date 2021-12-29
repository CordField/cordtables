package com.seedcompany.cordtables.components.tables.admin.role_table_permissions

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.TableNames
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.admin.role_table_permissions.roleTablePermission
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

data class AdminRoleTablePermissionsReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class AdminRoleTablePermissionsReadResponse(
    val error: ErrorType,
    val roleTablePermission: roleTablePermission? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleTablePermissionsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("admin-role-table-permissions/read")
    @ResponseBody
    fun readHandler(@RequestBody req: AdminRoleTablePermissionsReadRequest): AdminRoleTablePermissionsReadResponse {

        if (req.token == null) return AdminRoleTablePermissionsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return AdminRoleTablePermissionsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.role_table_permissions",
                getList = false,
                columns = arrayOf(
                    "id",
                    "role",
                    "table_name",
                    "table_permission",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group",
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var role: Int? = jdbcResult.getInt("role")
                if (jdbcResult.wasNull()) role = null

                var table_name: String? = jdbcResult.getString("table_name")
                if (jdbcResult.wasNull()) table_name = null

                var table_permission: String? = jdbcResult.getString("table_permission")
                if (jdbcResult.wasNull()) table_permission = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val roleTablePermission =
                    roleTablePermission(
                        id = id,
                        role = role,
                        table_name = table_name, // if (table_name == null) null else TableNames.valueOf(table_name),
                        table_permission = if (table_permission == null) null else TablePermissionGrantTypes.valueOf(table_permission),
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return AdminRoleTablePermissionsReadResponse(ErrorType.NoError, roleTablePermission = roleTablePermission)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return AdminRoleTablePermissionsReadResponse(ErrorType.SQLReadError)
        }

        return AdminRoleTablePermissionsReadResponse(error = ErrorType.UnknownError)
    }
}