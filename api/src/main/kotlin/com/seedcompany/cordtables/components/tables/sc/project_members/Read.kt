package com.seedcompany.cordtables.components.tables.sc.project_members

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

data class ScProjectMembersReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScProjectMembersReadResponse(
    val error: ErrorType,
    val projectMember: projectMember? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProjectMembersRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/project-members/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScProjectMembersReadRequest): ScProjectMembersReadResponse {

      if (req.token == null) return ScProjectMembersReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScProjectMembersReadResponse(ErrorType.AdminOnly)

        if (req.id == null) return ScProjectMembersReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.project_members",
                getList = false,
                columns = arrayOf(
                    "id",
                    "project",
                    "person",
                    "group_id",
                    "role",
                    "sensitivity",
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

                var project: String? = jdbcResult.getString("project")
                if (jdbcResult.wasNull()) project = null

                var person: String? = jdbcResult.getString("person")
                if (jdbcResult.wasNull()) person = null

                var group_id: String? = jdbcResult.getString("group_id")
                if (jdbcResult.wasNull()) group_id = null

                var role: String? = jdbcResult.getString("role")
                if (jdbcResult.wasNull()) role = null

                var sensitivity: String? = jdbcResult.getString("sensitivity")
                if (jdbcResult.wasNull()) sensitivity = null


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

                val projectMember =
                    projectMember(
                        id = id,
                        project = project,
                        person = person,
                        group_id = group_id,
                        role = role,
                        sensitivity = sensitivity,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScProjectMembersReadResponse(ErrorType.NoError, projectMember = projectMember)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScProjectMembersReadResponse(ErrorType.SQLReadError)
        }

        return ScProjectMembersReadResponse(error = ErrorType.UnknownError)
    }
}
