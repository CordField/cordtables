package com.seedcompany.cordtables.components.tables.sc.person_unavailabilities

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.personUnavailability
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


data class ScPersonUnavailabilitiesListRequest(
    val token: String?
)

data class ScPersonUnavailabilitiesListResponse(
    val error: ErrorType,
    val personUnavailabilities: MutableList<personUnavailability>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPersonUnavailabilitiesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/person-unavailabilities/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScPersonUnavailabilitiesListRequest): ScPersonUnavailabilitiesListResponse {
        var data: MutableList<personUnavailability> = mutableListOf()
        if (req.token == null) return ScPersonUnavailabilitiesListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.person_unavailabilities",
                filter = "order by id",
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
                )
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: Int? = jdbcResult.getInt("id")
                if (jdbcResult.wasNull()) id = null


                var person: Int? = jdbcResult.getInt("person")
                if (jdbcResult.wasNull()) person = null

                var period_start: String? = jdbcResult.getString("period_start")
                if (jdbcResult.wasNull()) period_start = null

                var period_end: String? = jdbcResult.getString("period_end")
                if (jdbcResult.wasNull()) period_end = null

                var description: String? = jdbcResult.getString("description")
                if (jdbcResult.wasNull()) description = null


                var created_by: Int? = jdbcResult.getInt("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: Int? = jdbcResult.getInt("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: Int? = jdbcResult.getInt("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: Int? = jdbcResult.getInt("owning_group")
                if (jdbcResult.wasNull()) owning_group = null

                data.add(
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
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScPersonUnavailabilitiesListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScPersonUnavailabilitiesListResponse(ErrorType.NoError, data)
    }
}

