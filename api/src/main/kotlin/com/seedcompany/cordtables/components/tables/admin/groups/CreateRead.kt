package com.seedcompany.cordtables.components.tables.admin.groups

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.groups.*
import com.seedcompany.cordtables.components.tables.admin.groups.AdminGroupsCreateRequest
import com.seedcompany.cordtables.components.tables.admin.groups.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupsCreateReadRequest(
    val token: String? = null,
    val group: groupInput,
)

data class AdminGroupsCreateReadResponse(
    val error: ErrorType,
    val group: group? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupsCreateRead")
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
    @PostMapping("admin-groups/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: AdminGroupsCreateReadRequest): AdminGroupsCreateReadResponse {

        val createResponse = create.createHandler(
            AdminGroupsCreateRequest(
                token = req.token,
                group = req.group
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return AdminGroupsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            AdminGroupsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return AdminGroupsCreateReadResponse(error = readResponse.error, group = readResponse.group)
    }
}