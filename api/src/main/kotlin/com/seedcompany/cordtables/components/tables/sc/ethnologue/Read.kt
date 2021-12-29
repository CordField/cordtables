package com.seedcompany.cordtables.components.tables.sc.ethnologue

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ethnologue
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

data class ScEthnologueReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class ScEthnologueReadResponse(
    val error: ErrorType,
    val ethnologue: ethnologue? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScEthnologueRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/ethnologue/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScEthnologueReadRequest): ScEthnologueReadResponse {

        if (req.token == null) return ScEthnologueReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScEthnologueReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.ethnologue",
                getList = false,
                columns = arrayOf(
                    "id",
                    "neo4j_id",
                    "language_index",
                    "code",
                    "language_name",
                    "population",
                    "provisional_code",
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

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null

                var neo4j_id: String? = jdbcResult.getString("neo4j_id")
                if (jdbcResult.wasNull()) neo4j_id = null

                var language_index: Int? = jdbcResult.getInt("language_index")
                if (jdbcResult.wasNull()) language_index = null

                var code: String? = jdbcResult.getString("code")
                if (jdbcResult.wasNull()) code = null

                var language_name: String? = jdbcResult.getString("language_name")
                if (jdbcResult.wasNull()) language_name = null

                var population: Int? = jdbcResult.getInt("population")
                if (jdbcResult.wasNull()) population = null

                var provisional_code: String? = jdbcResult.getString("provisional_code")
                if (jdbcResult.wasNull()) provisional_code = null

                var sensitivity: String? = jdbcResult.getString("sensitivity")
                if (jdbcResult.wasNull()) sensitivity = null

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

                val ethnologue =
                    ethnologue(
                        id = id,
                        neo4j_id = neo4j_id,
                        language_index = language_index,
                        code = code,
                        language_name = language_name,
                        population = population,
                        provisional_code = provisional_code,
                        sensitivity = sensitivity,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScEthnologueReadResponse(ErrorType.NoError, ethnologue = ethnologue)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScEthnologueReadResponse(ErrorType.SQLReadError)
        }

        return ScEthnologueReadResponse(error = ErrorType.UnknownError)
    }
}
