package com.seedcompany.cordtables.components.tables.common.organizations

import com.seedcompany.cordtables.common.*
import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.budget_records.Update

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrganizationsCreateRequest(
        val token: String? = null,
        val organization: CommonOrganizationsInput,
)

data class CommonOrganizationsCreateResponse(
        val error: ErrorType,
        val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrganizationsCreate")
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

    @PostMapping("common/organizations/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonOrganizationsCreateRequest): CommonOrganizationsCreateResponse {

        if (req.token == null) return CommonOrganizationsCreateResponse(error = ErrorType.InputMissingToken, null)

        // check enums and error out if needed
        if (!enumContains<CommonSensitivity>(req.organization.sensitivity)) {
            return CommonOrganizationsCreateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }



        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
                """
            insert into common.organizations(name, sensitivity, primary_location, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::common.sensitivity,
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
                req.organization.name,
                req.organization.sensitivity,
                req.organization.primary_location,
                req.token,
                req.token,
                req.token,
                util.adminGroupId
        )

//        req.language.id = id

        return CommonOrganizationsCreateResponse(error = ErrorType.NoError, id = id)
    }


}
