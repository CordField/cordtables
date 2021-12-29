package com.seedcompany.cordtables.components.tables.admin.group_row_access

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.group_row_access.*
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessCreateRequest
import com.seedcompany.cordtables.components.tables.admin.group_row_access.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupRowAccessCreateReadRequest(
    val token: String? = null,
    val groupRowAccess: groupRowAccessInput,
)

data class AdminGroupRowAccessCreateReadResponse(
    val error: ErrorType,
    val groupRowAccess: groupRowAccess? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupRowAccessCreateRead")
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
    @PostMapping("admin/group-row-access/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: AdminGroupRowAccessCreateReadRequest): AdminGroupRowAccessCreateReadResponse {

        val createResponse = create.createHandler(
            AdminGroupRowAccessCreateRequest(
                token = req.token,
                groupRowAccess = req.groupRowAccess
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return AdminGroupRowAccessCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            AdminGroupRowAccessReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return AdminGroupRowAccessCreateReadResponse(error = readResponse.error, groupRowAccess = readResponse.groupRowAccess)
    }
}
