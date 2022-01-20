package com.seedcompany.cordtables.components.tables.admin.users

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.roles.AdminRolesUpdateReadResponse
import com.seedcompany.cordtables.components.tables.admin.users.userInput
import com.seedcompany.cordtables.components.tables.admin.users.Read
import com.seedcompany.cordtables.components.tables.admin.users.Update
import com.seedcompany.cordtables.components.user.RegisterReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminUsersCreateRequest(
    val token: String? = null,
    val user: userInput,
)

data class AdminUsersCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminUsersCreate")
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

    @PostMapping("admin/users/create")
    @ResponseBody
    fun createHandler(@RequestBody req: AdminUsersCreateRequest): AdminUsersCreateResponse {

      if (req.token == null) return AdminUsersCreateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminUsersCreateResponse(ErrorType.AdminOnly)

        // if (req.user.name == null) return usersCreateResponse(error = ErrorType.InputMissingToken, null)
        if (req.user.email == null || !util.isEmailValid(req.user.email)) return AdminUsersCreateResponse(ErrorType.InvalidEmail)
        if (req.user.password == null || req.user.password.length < 8) return AdminUsersCreateResponse(ErrorType.PasswordTooShort)
        if (req.user.password.length > 32) return AdminUsersCreateResponse(ErrorType.PasswordTooLong)

        val pash = util.encoder.encode(req.user.password)

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into admin.users(id, email, password, created_by, modified_by, owning_person, owning_group)
                values(
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
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.user.id,
            req.user.email,
            pash,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

        return AdminUsersCreateResponse(error = ErrorType.NoError, id = id)
    }

}
