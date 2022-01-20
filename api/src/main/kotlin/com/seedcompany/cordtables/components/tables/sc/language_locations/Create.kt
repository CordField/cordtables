package com.seedcompany.cordtables.components.tables.sc.language_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.language_locations.languageLocationInput
import com.seedcompany.cordtables.components.tables.sc.language_locations.Read
import com.seedcompany.cordtables.components.tables.sc.language_locations.Update
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguageLocationsCreateRequest(
    val token: String? = null,
    val languageLocation: languageLocationInput,
)

data class ScLanguageLocationsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageLocationsCreate")
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

    @PostMapping("sc/language-locations/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScLanguageLocationsCreateRequest): ScLanguageLocationsCreateResponse {

      if (req.token == null) return ScLanguageLocationsCreateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScLanguageLocationsCreateResponse(ErrorType.AdminOnly)
        // if (req.languageLocation.name == null) return ScLanguageLocationsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.language_locations(language, location,  created_by, modified_by, owning_person, owning_group)
                values(
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
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.languageLocation.language,
            req.languageLocation.location,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

        return ScLanguageLocationsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
