package com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.budgetRecordsPartnershipInput
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.Read
import com.seedcompany.cordtables.components.tables.sc.budget_records_partnerships.Update
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetRecordsPartnershipsCreateRequest(
    val token: String? = null,
    val budgetRecordsPartnership: budgetRecordsPartnershipInput,
)

data class ScBudgetRecordsPartnershipsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetRecordsPartnershipsCreate")
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

    @PostMapping("sc/budget-records-partnerships/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScBudgetRecordsPartnershipsCreateRequest): ScBudgetRecordsPartnershipsCreateResponse {

      if (req.token == null) return ScBudgetRecordsPartnershipsCreateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScBudgetRecordsPartnershipsCreateResponse(ErrorType.AdminOnly)
        // if (req.budgetRecordsPartnership.name == null) return ScBudgetRecordsPartnershipsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.budget_records_partnerships(budget_record, partnership,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
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
            req.budgetRecordsPartnership.budget_record,
            req.budgetRecordsPartnership.partnership,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return ScBudgetRecordsPartnershipsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
