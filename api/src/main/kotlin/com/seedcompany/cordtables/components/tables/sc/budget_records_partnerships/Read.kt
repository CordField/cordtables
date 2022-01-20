package com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships

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

data class ScBudgetRecordsPartnershipsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScBudgetRecordsPartnershipsReadResponse(
    val error: ErrorType,
    val budgetRecordsPartnership: budgetRecordsPartnership? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetRecordsPartnershipsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/budget-records-partnerships/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScBudgetRecordsPartnershipsReadRequest): ScBudgetRecordsPartnershipsReadResponse {

      if (req.token == null) return ScBudgetRecordsPartnershipsReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScBudgetRecordsPartnershipsReadResponse(ErrorType.AdminOnly)

        if (req.id == null) return ScBudgetRecordsPartnershipsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.budget_records_partnerships",
                getList = false,
                columns = arrayOf(
                    "id",
                    "budget_record",
                    "partnership",
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

                var budget_record: String? = jdbcResult.getString("budget_record")
                if (jdbcResult.wasNull()) budget_record = null

                var partnership: String? = jdbcResult.getString("partnership")
                if (jdbcResult.wasNull()) partnership = null

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

                val budgetRecordsPartnership =
                    budgetRecordsPartnership(
                        id = id,
                      budget_record = budget_record,
                      partnership = partnership,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group,
                    )

                return ScBudgetRecordsPartnershipsReadResponse(ErrorType.NoError, budgetRecordsPartnership = budgetRecordsPartnership)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScBudgetRecordsPartnershipsReadResponse(ErrorType.SQLReadError)
        }

        return ScBudgetRecordsPartnershipsReadResponse(error = ErrorType.UnknownError)
    }
}
