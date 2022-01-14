package com.seedcompany.cordtables.components.tables.sc.budget_records

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
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
    val budget_record: BudgetRecordInput,
)

data class ScBudgetRecordsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
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

    @PostMapping("sc/budget-records/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScBudgetRecordsCreateRequest): ScBudgetRecordsCreateResponse {

        if (req.token == null) return ScBudgetRecordsCreateResponse(error = ErrorType.InputMissingToken, null)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.budget_records(budget, change_to_plan, active, amount, fiscal_year, organization, sensitivity,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
                    ?::boolean,
                    ?::decimal,
                    ?::integer,
                    ?::uuid,
                    ?::common.sensitivity,
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
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.budget_record.budget,
            req.budget_record.change_to_plan,
            req.budget_record.active,
            req.budget_record.amount,
            req.budget_record.fiscal_year,
            req.budget_record.organization,
            req.budget_record.sensitivity,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.budget_record.id = id

        return ScBudgetRecordsCreateResponse(error = ErrorType.NoError, id = id)
    }


}
