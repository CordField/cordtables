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


data class ScBudgetRecordsListRequest(
    val token: String?
)

data class ScBudgetRecordsListResponse(
    val error: ErrorType,
    val budget_records: MutableList<BudgetRecord>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetRecordsList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("sc-budget-records/list")
    @ResponseBody
    fun listHandler(@RequestBody req: ScBudgetRecordsListRequest): ScBudgetRecordsListResponse {
        var data: MutableList<BudgetRecord> = mutableListOf()
        if (req.token == null) return ScBudgetRecordsListResponse(ErrorType.TokenNotFound, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)

        val query =  secureList.getSecureListQueryHandler(
            GetSecureListQueryRequest(
                tableName = "sc.budget_records",
                filter = "order by id",
                columns = arrayOf(
                    "id",
                    "budget",
                    "change_to_plan",
                    "active",
                    "amount",
                    "fiscal_year",
                    "partnership",
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

                var budget: Int? = jdbcResult.getInt("budget")
                if (jdbcResult.wasNull()) budget = null

                var change_to_plan: Int? = jdbcResult.getInt("change_to_plan")
                if (jdbcResult.wasNull()) change_to_plan = null

                var active: Boolean? = jdbcResult.getBoolean("active")
                if (jdbcResult.wasNull()) active = null

                var amount: Double? = jdbcResult.getDouble("amount")
                if (jdbcResult.wasNull()) amount = null

                var fiscal_year: Int? = jdbcResult.getInt("fiscal_year")
                if (jdbcResult.wasNull()) fiscal_year = null

                var partnership: Int? = jdbcResult.getInt("partnership")
                if (jdbcResult.wasNull()) partnership = null

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

                data.add(
                    BudgetRecord(
                        id = id,
                        budget = budget,
                        change_to_plan = change_to_plan,
                        active = active,
                        amount = amount,
                        fiscal_year = fiscal_year,
                        partnership = partnership,
                        created_at = created_at,
                        created_by = created_by,
                        modified_at = modified_at,
                        modified_by = modified_by,
                        owning_person = owning_person,
                        owning_group = owning_group
                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return ScBudgetRecordsListResponse(ErrorType.SQLReadError, mutableListOf())
        }

        return ScBudgetRecordsListResponse(ErrorType.NoError, data)
    }
}

