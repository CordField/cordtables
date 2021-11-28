package com.seedcompany.cordtables.components.tables.common.people_to_org_relationships

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.common.people_to_org_relationships.peopleToOrgRelationship
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

data class CommonPeopleToOrgRelationshipsReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class CommonPeopleToOrgRelationshipsReadResponse(
    val error: ErrorType,
    val peopleToOrgRelationship: peopleToOrgRelationship? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonPeopleToOrgRelationshipsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common-people-to-org-relationships/read")
    @ResponseBody
    fun readHandler(@RequestBody req: CommonPeopleToOrgRelationshipsReadRequest): CommonPeopleToOrgRelationshipsReadResponse {

        if (req.token == null) return CommonPeopleToOrgRelationshipsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonPeopleToOrgRelationshipsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "common.people_to_org_relationships",
                getList = false,
                columns = arrayOf(
                    "id",
                    "org",
                    "person",
                    "relationship_type",
                    "begin_at",
                    "end_at",
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

                var org: Int? = jdbcResult.getInt("org")
                if (jdbcResult.wasNull()) org = null

                var person: Int? = jdbcResult.getInt("person")
                if (jdbcResult.wasNull()) person = null

                var relationship_type: String? = jdbcResult.getString("relationship_type")
                if (jdbcResult.wasNull()) relationship_type = null

                var begin_at: String? = jdbcResult.getString("begin_at")
                if (jdbcResult.wasNull()) begin_at = null

                var end_at: String? = jdbcResult.getString("end_at")
                if (jdbcResult.wasNull()) end_at = null

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

                val peopleToOrgRelationship =
                    peopleToOrgRelationship(
                        id = id,
                        org = org,
                        person = person,
                        relationship_type = relationship_type,
                        begin_at = begin_at,
                        end_at = end_at,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return CommonPeopleToOrgRelationshipsReadResponse(ErrorType.NoError, peopleToOrgRelationship = peopleToOrgRelationship)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonPeopleToOrgRelationshipsReadResponse(ErrorType.SQLReadError)
        }

        return CommonPeopleToOrgRelationshipsReadResponse(error = ErrorType.UnknownError)
    }
}