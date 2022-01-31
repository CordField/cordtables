package com.seedcompany.cordtables.components.tables.sc.budget_records

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

data class ScBudgetRecordsReadRequest(
    val token: String?,
    val id: String? = null,
)

data class ScBudgetRecordsReadResponse(
    val error: ErrorType,
    val budget_record: BudgetRecord? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetRecordsRead")
class Read(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc/budget-records/read")
    @ResponseBody
    fun readHandler(@RequestBody req: ScBudgetRecordsReadRequest): ScBudgetRecordsReadResponse {

        if (req.token == null) return ScBudgetRecordsReadResponse(ErrorType.TokenNotFound)
        if (req.id == null) return ScBudgetRecordsReadResponse(ErrorType.MissingId)

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("id", req.id)

        val query = secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.budget_records",
                getList = false,
                columns = arrayOf(
                    "id",
                    "budget",
                    "change_to_plan",
                    "active",
                    "amount",
                    "fiscal_year",
                    "organization",
                    "sensitivity",
                    "created_at",
                    "created_by",
                    "modified_at",
                    "modified_by",
                    "owning_person",
                    "owning_group"
                ),
            )
        ).query

        try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var budget: String? = jdbcResult.getString("budget")
                if (jdbcResult.wasNull()) budget = null

                var change_to_plan: String? = jdbcResult.getString("change_to_plan")
                if (jdbcResult.wasNull()) change_to_plan = null

                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null

                var amount: Double? = jdbcResult.getDouble("amount")
                if (jdbcResult.wasNull()) amount = null

                var fiscal_year: Int? = jdbcResult.getInt("fiscal_year")
                if (jdbcResult.wasNull()) fiscal_year = null

                var organization: String? = jdbcResult.getString("organization")
                if (jdbcResult.wasNull()) organization = null

                var sensitivity: String? = jdbcResult.getString("sensitivity")
                if (jdbcResult.wasNull()) sensitivity = null

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

                val budget_record =
                    BudgetRecord(
                        id = id,
                        budget = budget,
                        change_to_plan = change_to_plan,
                        active = active,
                        amount = amount,
                        fiscal_year = fiscal_year,
                        organization = organization,
                        sensitivity = sensitivity,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group
                    )

                return ScBudgetRecordsReadResponse(ErrorType.NoError, budget_record = budget_record)

            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScBudgetRecordsReadResponse(ErrorType.SQLReadError)
        }

        return ScBudgetRecordsReadResponse(error = ErrorType.UnknownError)
    }
}
