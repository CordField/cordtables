package com.seedcompany.cordtables.components.tables.admin.role_memberships
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource
import kotlin.collections.List


data class GlobalRoleMemberships(
        val id: Int?,
        val globalRole: Int?,
        val person: Int?,
        val createdAt: String?,
        val createdBy: Int?,
        val modifiedAt: String?,
        val modifiedBy: Int?,
        val owningPerson: Int?,
        val owningGroup: Int?,
)

data class GlobalRoleMembershipsRequest(
        val token: String? = null,
)

data class GlobalRoleMembershipsReturn(
        val error: ErrorType,
        val globalRoleMemberships: List<out GlobalRoleMemberships>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GlobalRoleMembershipsList")
class List(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("role_memberships/list")
    @ResponseBody
    fun listHandler(@RequestBody req: GlobalRoleMembershipsRequest): GlobalRoleMembershipsReturn {

        if (req.token == null) return GlobalRoleMembershipsReturn(ErrorType.TokenNotFound, null)

        val items = mutableListOf<GlobalRoleMemberships>()

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "admin.role_memberships",
                columns = arrayOf(
                    "id",
                    "globalRole",
                    "person",
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

                var globalRole: Int? = jdbcResult.getInt("role")
                if (jdbcResult.wasNull()) globalRole = null

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

                var owningPerson: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owningPerson = null

                var owningGroup: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owningGroup = null

                items.add(
                    GlobalRoleMemberships(
                        id = id,
                        globalRole = globalRole,
                        person = person,
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
            println("error while listing ${e.message}")
            return GlobalRoleMembershipsReturn(ErrorType.SQLReadError, mutableListOf())
        }

        return GlobalRoleMembershipsReturn(ErrorType.NoError, items)
    }

}