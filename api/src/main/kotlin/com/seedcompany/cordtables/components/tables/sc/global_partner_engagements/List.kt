package com.seedcompany.cordtables.components.tables.sc.global_partner_engagements

import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.globalPartnerEngagement
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


data class ScGlobalPartnerEngagementsListRequest(
    val token: String?
)

data class ScGlobalPartnerEngagementsListResponse(
    val error: ErrorType,
    val globalPartnerEngagements: MutableList<globalPartnerEngagement>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerEngagementsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-global-partner-engagements/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScGlobalPartnerEngagementsListRequest): ScGlobalPartnerEngagementsListResponse {
        var data: MutableList<globalPartnerEngagement> = mutableListOf()
        if (req.token == null) return ScGlobalPartnerEngagementsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.global_partner_engagements",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "organization",
                    "type",
                    "mou_start",
                    "mou_end",
                    "sc_roles",
                    "partner_roles",
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

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var organization: String? = jdbcResult.getString("organization")
                if (jdbcResult.wasNull()) organization = null

                var type: String? = jdbcResult.getString("type")
                if (jdbcResult.wasNull()) type = null

                var mou_start: String? = jdbcResult.getString("mou_start")
                if (jdbcResult.wasNull()) mou_start = null

                var mou_end: String? = jdbcResult.getString("mou_end")
                if (jdbcResult.wasNull()) mou_end = null

                var sc_roles: String? = jdbcResult.getString("sc_roles")
                if (jdbcResult.wasNull()) sc_roles = null

                var partner_roles: String? = jdbcResult.getString("partner_roles")
                if (jdbcResult.wasNull()) partner_roles = null

                var created_by: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) created_by = null

                var created_at: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) created_at = null

                var modified_at: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modified_at = null

                var modified_by: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modified_by = null

                var owning_person: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owning_person = null

                var owning_group: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owning_group = null


                data.add(
                    globalPartnerEngagement(
                        id = id,
                        organization = organization,
                        type = type, //if (type == null) null else InvolvementOptions.valueOf(type),
                        mou_start = mou_start,
                        mou_end = mou_end,
                        sc_roles = sc_roles, // if (sc_roles == null) null else GlobalPartnerRoles.valueOf(sc_roles),
                        partner_roles = partner_roles, //if (partner_roles == null) null else GlobalPartnerRoles.valueOf(partner_roles),
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
            return ScGlobalPartnerEngagementsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScGlobalPartnerEngagementsListResponse(ErrorType.NoError, data)
    }
}
