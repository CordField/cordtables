package com.seedcompany.cordtables.components.tables.sc.budgetrecords

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.budget_records.BudgetRecordInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetRecordsCreateRequest(
    val token: String? = null,
    val budgetrecord: BudgetRecordInput,
)

data class ScBudgetRecordsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScBudgetRecordsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("sc-budget-records/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScBudgetRecordsCreateRequest): ScBudgetRecordsCreateResponse {

        if (req.token == null) return ScBudgetRecordsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.budgetrecords(budget, change_to_plan, fiscal_year, partnership, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?,
                    ?,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.budgetrecord.budget,
            req.budgetrecord.change_to_plan,
            req.budgetrecord.fiscal_year,
            req.budgetrecord.partnership,
            req.token,
            req.token,
            req.token,
        )

//        req.budgetrecord.id = id

        return ScBudgetRecordsCreateResponse(error = ErrorType.NoError, id = id)
    }


}