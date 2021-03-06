package com.seedcompany.cordtables.components.tables.common.stage_role_column_grants

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

data class CommonStageRoleColumnGrantsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class CommonStageRoleColumnGrantsReadResponse(
    val error: ErrorType,
    val stageRoleColumnGrant: stageRoleColumnGrant? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageRoleColumnGrantsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/stage-role-column-grants/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonStageRoleColumnGrantsReadRequest): CommonStageRoleColumnGrantsReadResponse {

        if (req.token == null) return CommonStageRoleColumnGrantsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonStageRoleColumnGrantsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.stage_role_column_grants",
                getList = false,
                columns = arrayOf(
                    "id",
                    "stage",
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
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var stage: String? = jdbcResult.getString("stage")
                if (jdbcResult.wasNull()) stage = null

                var role: String? = jdbcResult.getString("role")
                if (jdbcResult.wasNull()) role = null

                var table_name: String? = jdbcResult.getString("table_name")
                if (jdbcResult.wasNull()) table_name = null

                var column_name: String? = jdbcResult.getString("column_name")
                if (jdbcResult.wasNull()) column_name = null

                var access_level: String? = jdbcResult.getString("access_level")
                if (jdbcResult.wasNull()) access_level = null

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

                val stageRoleColumnGrant =
                    stageRoleColumnGrant(
                        id = id,
                        stage = stage,
                        role = role,
                        table_name = table_name,
                        column_name = column_name,
                        access_level = access_level,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return CommonStageRoleColumnGrantsReadResponse(ErrorType.NoError, stageRoleColumnGrant = stageRoleColumnGrant)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonStageRoleColumnGrantsReadResponse(ErrorType.SQLReadError)
        }

        return CommonStageRoleColumnGrantsReadResponse(error = ErrorType.UnknownError)
    }
}
