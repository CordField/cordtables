package com.seedcompany.cordtables.components.tables.admin.group_memberships

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.admin.group_memberships.groupMembership
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocation
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

data class AdminGroupMembershipsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class AdminGroupMembershipsReadResponse(
    val error: ErrorType,
    val groupMembership: groupMembership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupMembershipsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("admin/group-memberships/read")
    @ResponseBody
    fun readHandler(@RequestBody req: AdminGroupMembershipsReadRequest): AdminGroupMembershipsReadResponse {

      if (req.token == null) return AdminGroupMembershipsReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminGroupMembershipsReadResponse(ErrorType.AdminOnly)

        if (req.id == null) return AdminGroupMembershipsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.group_memberships",
                getList = false,
                columns = arrayOf(
                    "id",
                    "group_id",
                    "person",
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

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var group_id: String? = jdbcResult.getString("group_id")
                if (jdbcResult.wasNull()) group_id = null

                var person: String? = jdbcResult.getString("person")
                if (jdbcResult.wasNull()) person = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                val groupMembership =
                    groupMembership(
                        id = id,
                        group_id = group_id,
                        person = person,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return AdminGroupMembershipsReadResponse(ErrorType.NoError, groupMembership = groupMembership)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return AdminGroupMembershipsReadResponse(ErrorType.SQLReadError)
        }

        return AdminGroupMembershipsReadResponse(error = ErrorType.UnknownError)
    }
}
