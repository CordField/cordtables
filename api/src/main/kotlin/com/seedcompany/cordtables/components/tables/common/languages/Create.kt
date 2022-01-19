package com.seedcompany.cordtables.components.tables.common.languages

import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.locations.CommonLocationsCreateRequest
import com.seedcompany.cordtables.components.tables.common.locations.CommonLocationsCreateResponse
import com.seedcompany.cordtables.components.tables.common.locations.Read
import com.seedcompany.cordtables.components.tables.common.locations.Update
import com.seedcompany.cordtables.components.tables.common.locations.locationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonLanguagesCreateRequest(
  val token: String? = null,
)

data class CommonLanguagesCreateResponse(
  val error: ErrorType,
  val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonLanguagesCreate")
class Create(
  @Autowired
  val util: Utility,

  @Autowired
  val ds: DataSource,

) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  @PostMapping("common/languages/create")
  @ResponseBody
  fun createHandler(@RequestBody req: CommonLanguagesCreateRequest): CommonLanguagesCreateResponse {

    if (req.token == null) return CommonLanguagesCreateResponse(error = ErrorType.InputMissingToken, null)

    val id = jdbcTemplate.queryForObject(
      """
            insert into common.languages(
                created_by, 
                modified_by, 
                owning_person, 
                owning_group)
            values(
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
      req.token,
      req.token,
      req.token,
      util.adminGroupId()
    )

    return CommonLanguagesCreateResponse(error = ErrorType.NoError, id = id)
  }


}
