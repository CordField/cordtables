package com.seedcompany.cordtables.components.tables.admin.group_row_access

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessReadRequest
import com.seedcompany.cordtables.components.tables.admin.group_row_access.AdminGroupRowAccessUpdateRequest
import com.seedcompany.cordtables.components.tables.admin.group_row_access.groupRowAccess
import com.seedcompany.cordtables.components.tables.admin.group_row_access.groupRowAccessInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminGroupRowAccessUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminGroupRowAccessUpdateReadResponse(
    val error: ErrorType,
    val groupRowAccess: groupRowAccess? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminGroupRowAccessUpdateRead")
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
    @PostMapping("admin-group-row-access/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: AdminGroupRowAccessUpdateReadRequest): AdminGroupRowAccessUpdateReadResponse {

        val updateResponse = update.updateHandler(
            AdminGroupRowAccessUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return AdminGroupRowAccessUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            AdminGroupRowAccessReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return AdminGroupRowAccessUpdateReadResponse(error = readResponse.error, readResponse.groupRowAccess)
    }
}