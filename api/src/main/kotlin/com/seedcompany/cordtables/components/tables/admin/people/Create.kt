package com.seedcompany.cordtables.components.tables.admin.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.people.peopleInput
import com.seedcompany.cordtables.components.tables.admin.people.Read
import com.seedcompany.cordtables.components.tables.admin.people.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminPeopleCreateRequest(
    val token: String? = null,
    val people: peopleInput,
)

data class AdminPeopleCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("AdminPeopleCreate")
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

    @PostMapping("admin-people/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminPeopleCreateRequest): AdminPeopleCreateResponse {

        // if (req.people.name == null) return AdminPeopleCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into admin.people(about, phone, picture, private_first_name, private_last_name, public_first_name, public_last_name, primary_location,
             private_full_name, public_full_name, sensitivity_clearance, time_zone, title, status,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
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
            req.people.about,
            req.people.phone,
            req.people.picture,
            req.people.private_first_name,
            req.people.private_last_name,
            req.people.public_first_name,
            req.people.public_last_name,
            req.people.primary_location,
            req.people.private_full_name,
            req.people.public_full_name,
            req.people.sensitivity_clearance,
            req.people.time_zone,
            req.people.title,
            req.people.status,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return AdminPeopleCreateResponse(error = ErrorType.NoError, id = id)
    }

}
