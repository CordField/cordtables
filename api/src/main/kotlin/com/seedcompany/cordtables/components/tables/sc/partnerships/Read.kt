package com.seedcompany.cordtables.components.tables.sc.partnerships

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.partnerships.partnership
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

data class ScPartnershipsReadRequest(
    val token: String?,
    val id: Int? = null,
)

data class ScPartnershipsReadResponse(
    val error: ErrorType,
    val partnership: partnership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScPartnershipsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-partnerships/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScPartnershipsReadRequest): ScPartnershipsReadResponse {

        if (req.token == null) return ScPartnershipsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScPartnershipsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.partnerships",
                getList = false,
                columns = arrayOf(
                    "id",
                    "project",
                    "partner",
                    "change_to_plan",
                    "active",
                    "agreement",
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

                var project: Int? = jdbcResult.getInt("project")
                if (jdbcResult.wasNull()) project = null

                var partner: Int? = jdbcResult.getInt("partner")
                if (jdbcResult.wasNull()) partner = null

                var change_to_plan: Int? = jdbcResult.getInt("change_to_plan")
                if (jdbcResult.wasNull()) change_to_plan = null

                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null

                var agreement: Int? = jdbcResult.getInt("agreement")
                if (jdbcResult.wasNull()) agreement = null

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

                val partnership =
                    partnership(
                        id = id,
                        project = project,
                        partner = partner,
                        change_to_plan = change_to_plan,
                        active = active,
                        agreement = agreement,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScPartnershipsReadResponse(ErrorType.NoError, partnership = partnership)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScPartnershipsReadResponse(ErrorType.SQLReadError)
        }

        return ScPartnershipsReadResponse(error = ErrorType.UnknownError)
    }
}