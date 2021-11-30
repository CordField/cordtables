package com.seedcompany.cordtables.components.tables.sc.global_partner_transitions

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.globalPartnerTransition
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


data class ScGlobalPartnerTransitionsListRequest(
    val token: String?
)

data class ScGlobalPartnerTransitionsListResponse(
    val error: ErrorType,
    val globalPartnerTransitions: MutableList<globalPartnerTransition>?
)


@Controller("ScGlobalPartnerTransitionsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-global-partner-transitions/list")
    @ResponseBody
    fun listHandler(@RequestBody req:ScGlobalPartnerTransitionsListRequest): ScGlobalPartnerTransitionsListResponse {
        var data: MutableList<globalPartnerTransition> = mutableListOf()
        if (req.token == null) return ScGlobalPartnerTransitionsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.global_partner_transitions",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "organization",
                    "transition_type",
                    "effective_date",
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

                var organization: Int? = jdbcResult.getInt("organization")
                if (jdbcResult.wasNull()) organization = null

                var transition_type: String? = jdbcResult.getString("transition_type")
                if (jdbcResult.wasNull()) transition_type = null

                var effective_date: String? = jdbcResult.getString("effective_date")
                if (jdbcResult.wasNull()) effective_date = null

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
                    globalPartnerTransition(
                        id = id,
                        organization = organization,
                        transition_type = transition_type,
                        effective_date = effective_date,
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
            return ScGlobalPartnerTransitionsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScGlobalPartnerTransitionsListResponse(ErrorType.NoError, data)
    }
}

