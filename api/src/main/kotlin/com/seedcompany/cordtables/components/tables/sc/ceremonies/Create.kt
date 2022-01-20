package com.seedcompany.cordtables.components.tables.sc.ceremonies

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.ceremonies.ceremonyInput
import com.seedcompany.cordtables.components.tables.sc.ceremonies.Read
import com.seedcompany.cordtables.components.tables.sc.ceremonies.Update
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScCeremoniesCreateRequest(
  val token: String? = null,
  val ceremony: ceremonyInput,
)

data class ScCeremoniesCreateResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScCeremoniesCreate")
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

  @PostMapping("sc/ceremonies/create")
  @ResponseBody
  fun createHandler(@RequestBody req: ScCeremoniesCreateRequest): ScCeremoniesCreateResponse {

    if (req.token == null) return ScCeremoniesCreateResponse(ErrorType.InputMissingToken)
    if (!util.isAdmin(req.token)) return ScCeremoniesCreateResponse(ErrorType.AdminOnly)
    // if (req.ceremony.name == null) return ScCeremoniesCreateResponse(error = ErrorType.InputMissingToken, null)


    // create row with required fields, use id to update cells afterwards one by one
    val id = jdbcTemplate.queryForObject(
      """
            insert into sc.ceremonies(internship_engagement, language_engagement, ethnologue, actual_date, estimated_date, is_planned, type,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?,
                    ?::timestamp,
                    ?::timestamp,
                    ?::boolean,
                    ?::common.ceremony_type,
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
                    ?
                )
            returning id;
        """.trimIndent(),
      String::class.java,
      req.ceremony.internship_engagement,
      req.ceremony.language_engagement,
      req.ceremony.ethnologue,
      req.ceremony.actual_date,
      req.ceremony.estimated_date,
      req.ceremony.is_planned,
      req.ceremony.type,
      req.token,
      req.token,
      req.token,
      util.adminGroupId()
    )

    return ScCeremoniesCreateResponse(error = ErrorType.NoError, id = id)
  }

}
