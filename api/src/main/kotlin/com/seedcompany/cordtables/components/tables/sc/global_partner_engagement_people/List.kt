package com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people

import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.globalPartnerEngagementPeople
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


data class ScGlobalPartnerEngagementPeopleListRequest(
    val token: String?
)

data class ScGlobalPartnerEngagementPeopleListResponse(
    val error: ErrorType,
    val globalPartnerEngagementPeoples: MutableList<globalPartnerEngagementPeople>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScGlobalPartnerEngagementPeopleList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-global-partner-engagement-people/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScGlobalPartnerEngagementPeopleListRequest): ScGlobalPartnerEngagementPeopleListResponse {
        var data: MutableList<globalPartnerEngagementPeople> = mutableListOf()
        if (req.token == null) return ScGlobalPartnerEngagementPeopleListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.global_partner_engagement_people",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "engagement",
                    "person",
                    "role",
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

                var engagement: Int? = jdbcResult.getInt("engagement")
                if (jdbcResult.wasNull()) engagement = null

                var person: Int? = jdbcResult.getInt("person")
                if (jdbcResult.wasNull()) person = null

                var role: String? = jdbcResult.getString("role")
                if (jdbcResult.wasNull()) role = null

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
                    globalPartnerEngagementPeople(
                        id = id,
                        engagement = engagement,
                        person = person,
                        role = role, // if (role == null) null else PeopleToOrgRelationshipType.valueOf(role),
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
            return ScGlobalPartnerEngagementPeopleListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScGlobalPartnerEngagementPeopleListResponse(ErrorType.NoError, data)
    }
}