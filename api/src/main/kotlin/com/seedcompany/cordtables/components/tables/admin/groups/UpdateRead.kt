package com.seedcompany.cordtables.components.tables.admin.groups

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.groups.AdminGroupsReadRequest
import com.seedcompany.cordtables.components.tables.admin.groups.AdminGroupsUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.groups.group
import com.seedcompany.cordtables.components.tables.admin.groups.groupInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminGroupsUpdateReadResponse(
    val error: ErrorType,
    val group: group? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupsUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("admin-groups/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: AdminGroupsUpdateReadRequest): AdminGroupsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            AdminGroupsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return AdminGroupsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            AdminGroupsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return AdminGroupsUpdateReadResponse(error = readResponse.error, readResponse.group)
    }
}