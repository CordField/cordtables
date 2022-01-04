package com.seedcompany.cordtables.components.tables.admin.users

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.users.*
import com.seedcompany.cordtables.components.tables.admin.users.AdminUsersCreateRequest
import com.seedcompany.cordtables.components.tables.admin.users.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminUsersCreateReadRequest(
    val token: String? = null,
    val user: userInput,
)

data class AdminUsersCreateReadResponse(
    val error: ErrorType,
    val user: user? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminUsersCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("admin/users/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: AdminUsersCreateReadRequest): AdminUsersCreateReadResponse {

      if (req.token == null) return AdminUsersCreateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return AdminUsersCreateReadResponse(ErrorType.AdminOnly)

        val createResponse = create.createHandler(
            AdminUsersCreateRequest(
                token = req.token,
                user = req.user
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return AdminUsersCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            AdminUsersReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return AdminUsersCreateReadResponse(error = readResponse.error, user = readResponse.user)
    }
}
