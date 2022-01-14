package com.seedcompany.cordtables.components.tables.sc.change_to_plans

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.changeToPlanInput
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.Read
import com.seedcompany.cordtables.components.tables.sc.change_to_plans.Update
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScChangeToPlansCreateRequest(
  val token: String? = null,
  val changeToPlan: changeToPlanInput,
)

data class ScChangeToPlansCreateResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScChangeToPlansCreate")
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

  @PostMapping("sc/change-to-plans/create")
  @ResponseBody
  fun createHandler(@RequestBody req: ScChangeToPlansCreateRequest): ScChangeToPlansCreateResponse {

    if (req.token == null) return ScChangeToPlansCreateResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScChangeToPlansCreateResponse(ErrorType.AdminOnly)
    // if (req.changeToPlan.name == null) return ScChangeToPlansCreateResponse(error = ErrorType.InputMissingToken, null)


    // create row with required fields, use id to update cells afterwards one by one
    val id = jdbcTemplate.queryForObject(
      """
            insert into sc.change_to_plans(status, summary, type, created_by, modified_by, owning_person, owning_group)
                values(
                    ?::sc.change_to_plan_status,
                    ?,
                    ?::sc.change_to_plan_type,
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
      req.changeToPlan.status,
      req.changeToPlan.summary,
      req.changeToPlan.type,
      req.token,
      req.token,
      req.token,
      util.adminGroupId
    )

//        req.language.id = id

    return ScChangeToPlansCreateResponse(error = ErrorType.NoError, id = id)
  }

}
