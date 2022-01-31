package com.seedcompany.cordtables.components.tables.sc.person_unavailabilities

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

data class ScPersonUnavailabilitiesReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScPersonUnavailabilitiesReadResponse(
    val error: ErrorType,
    val personUnavailability: personUnavailability? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPersonUnavailabilitiesRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/person-unavailabilities/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScPersonUnavailabilitiesReadRequest): ScPersonUnavailabilitiesReadResponse {

        if (req.token == null) return ScPersonUnavailabilitiesReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScPersonUnavailabilitiesReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.person_unavailabilities",
                getList = false,
                columns = arrayOf(
                    "id",
                    "person",
                    "period_start",
                    "period_end",
                    "description",
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

                var person: String? = jdbcResult.getString("person")
                if (jdbcResult.wasNull()) person = null

                var period_start: String? = jdbcResult.getString("period_start")
                if (jdbcResult.wasNull()) period_start = null

                var period_end: String? = jdbcResult.getString("period_end")
                if (jdbcResult.wasNull()) period_end = null

                var description: String? = jdbcResult.getString("description")
                if (jdbcResult.wasNull()) description = null

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

                val personUnavailability =
                    personUnavailability(
                        id = id,

                        person = person,
                        period_start = period_start,
                        period_end = period_end,
                        description = description,

                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScPersonUnavailabilitiesReadResponse(ErrorType.NoError, personUnavailability = personUnavailability)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScPersonUnavailabilitiesReadResponse(ErrorType.SQLReadError)
        }

        return ScPersonUnavailabilitiesReadResponse(error = ErrorType.UnknownError)
    }
}
