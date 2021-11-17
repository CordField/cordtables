package com.seedcompany.cordtables.components.tables.sc.organization_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.organization_locations.organizationLocationInput
import com.seedcompany.cordtables.components.tables.sc.organization_locations.Read
import com.seedcompany.cordtables.components.tables.sc.organization_locations.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScOrganizationLocationsCreateRequest(
    val token: String? = null,
    val organizationLocation: organizationLocationInput,
)

data class ScOrganizationLocationsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScOrganizationLocationsCreate")
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

    @PostMapping("sc-organization-locations/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScOrganizationLocationsCreateRequest): ScOrganizationLocationsCreateResponse {

        // if (req.organizationLocation.name == null) return ScOrganizationLocationsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.organization_locations(organization, location,  created_by, modified_by, owning_person, owning_group)
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
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.organizationLocation.organization,
            req.organizationLocation.location,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScOrganizationLocationsCreateResponse(error = ErrorType.NoError, id = id)
    }

}

