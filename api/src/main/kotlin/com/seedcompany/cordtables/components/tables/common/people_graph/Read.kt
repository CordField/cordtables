package com.seedcompany.cordtables.components.tables.common.people_graph

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.common.people_graph.peopleGraph
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

data class CommonPeopleGraphReadRequest(
    val token: String?,
    val id: String? = null,
)

data class CommonPeopleGraphReadResponse(
    val error: ErrorType,
    val peopleGraph: peopleGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleGraphRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/people-graph/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonPeopleGraphReadRequest): CommonPeopleGraphReadResponse {

        if (req.token == null) return CommonPeopleGraphReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonPeopleGraphReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.people_graph",
                getList = false,
                columns = arrayOf(
                    "id",
                    "from_person",
                    "to_person",
                    "rel_type",
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

                var from_person: Int? = jdbcResult.getInt("from_person")
                if (jdbcResult.wasNull()) from_person = null

                var to_person: Int? = jdbcResult.getInt("to_person")
                if (jdbcResult.wasNull()) to_person = null

                var rel_type: String? = jdbcResult.getString("rel_type")
                if (jdbcResult.wasNull()) rel_type = null

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

                val peopleGraph =
                    peopleGraph(
                        id = id,
                        from_person = from_person,
                        to_person = to_person,
                        rel_type = rel_type,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return CommonPeopleGraphReadResponse(ErrorType.NoError, peopleGraph = peopleGraph)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonPeopleGraphReadResponse(ErrorType.SQLReadError)
        }

        return CommonPeopleGraphReadResponse(error = ErrorType.UnknownError)
    }
}
