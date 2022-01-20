package com.seedcompany.cordtables.components.tables.sc.budgets

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.budgets.budgetInput
import com.seedcompany.cordtables.components.tables.sc.budgets.Read
import com.seedcompany.cordtables.components.tables.sc.budgets.Update
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScBudgetsCreateRequest(
  val token: String? = null,
  val budget: budgetInput,
)

data class ScBudgetsCreateResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetsCreate")
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

  @PostMapping("sc/budgets/create")
  @ResponseBody
  fun createHandler(@RequestBody req: ScBudgetsCreateRequest): ScBudgetsCreateResponse {

    if (req.token == null) return ScBudgetsCreateResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScBudgetsCreateResponse(ErrorType.AdminOnly)
    // if (req.budget.name == null) return ScBudgetsCreateResponse(error = ErrorType.InputMissingToken, null)


    // create row with required fields, use id to update cells afterwards one by one
    val id = jdbcTemplate.queryForObject(
      """
            insert into sc.budgets(change_to_plan, project, status, universal_template, universal_template_file_url, sensitivity, total,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
                    ?::common.budget_status,
                    ?::uuid,
                    ?,
                    ?::common.sensitivity,
                    ?::DECIMAL,
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
      req.budget.change_to_plan,
      req.budget.project,
      req.budget.status,
      req.budget.universal_template,
      req.budget.universal_template_file_url,
      req.budget.sensitivity,
      req.budget.total,
      req.token,
      req.token,
      req.token,
      util.adminGroupId()
    )

//        req.language.id = id

    return ScBudgetsCreateResponse(error = ErrorType.NoError, id = id)
  }

}
