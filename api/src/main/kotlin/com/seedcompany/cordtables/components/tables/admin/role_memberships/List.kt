package com.seedcompany.cordtables.components.tables.admin.role_memberships

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


data class AdminRoleMembershipsListRequest(
    val token: String?
)

data class AdminRoleMembershipsListResponse(
    val error: ErrorType,
    val roleMemberships: MutableList<roleMembership>? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminRoleMembershipsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("admin/role-memberships/list")
    @ResponseBody
    fun listHandler(@RequestBody req:AdminRoleMembershipsListRequest): AdminRoleMembershipsListResponse {

      if (req.token == null) return AdminRoleMembershipsListResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminRoleMembershipsListResponse(ErrorType.AdminOnly)

        var data: MutableList<roleMembership> = mutableListOf()
        if (req.token == null) return AdminRoleMembershipsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.role_memberships",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "role",
                    "person",
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
            println(jdbcResult.row)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var role: String? = jdbcResult.getString("role")
                if (jdbcResult.wasNull()) role = null

                var person: String? = jdbcResult.getString("person")
                if (jdbcResult.wasNull()) role = null

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
                    roleMembership(
                        id = id,
                        role = role,
                        person = person,
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
            return AdminRoleMembershipsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return AdminRoleMembershipsListResponse(ErrorType.NoError, data)
    }
}
