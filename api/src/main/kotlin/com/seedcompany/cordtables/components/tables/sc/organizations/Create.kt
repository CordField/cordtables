package com.seedcompany.cordtables.components.tables.sc.organizations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.organizations.organizationInput
import com.seedcompany.cordtables.components.tables.sc.organizations.Read
import com.seedcompany.cordtables.components.tables.sc.organizations.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScOrganizationsCreateRequest(
    val token: String? = null,
    val organization: organizationInput,
)

data class ScOrganizationsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScOrganizationsCreate")
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

    @PostMapping("sc/organizations/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScOrganizationsCreateRequest): ScOrganizationsCreateResponse {

        // if (req.organization.name == null) return ScOrganizationsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.organizations(id, sensitivity, root_directory, address,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::common.sensitivity,
                    ?::uuid,
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
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.organization.id,
            req.organization.sensitivity,
            req.organization.root_directory,
            req.organization.address,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

//        req.language.id = id

        return ScOrganizationsCreateResponse(error = ErrorType.NoError, id = id)
    }

}

