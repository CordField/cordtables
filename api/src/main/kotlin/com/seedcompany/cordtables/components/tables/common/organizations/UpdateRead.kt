package com.seedcompany.cordtables.components.tables.common.organizations

import com.seedcompany.cordtables.components.tables.common.organizations.*
import com.seedcompany.cordtables.components.tables.common.organizations.Read
import com.seedcompany.cordtables.components.tables.common.organizations.Update

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrganizationsUpdateReadRequest(
        val token: String?,
        val id: Int? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonOrganizationsUpdateReadResponse(
        val error: ErrorType,
        val organization: CommonOrganization? = null,
)


@Controller("CommonOrganizationsUpdateRead")
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
    @PostMapping("common-organizations/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonOrganizationsUpdateReadRequest): CommonOrganizationsUpdateReadResponse {

        val updateResponse = update.updateHandler(
                CommonOrganizationsUpdateRequest(
                        token = req.token,
                        column = req.column,
                        id = req.id,
                        value = req.value,
                )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonOrganizationsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
                CommonOrganizationsReadRequest(
                        token = req.token,
                        id = req.id!!
                )
        )

        return CommonOrganizationsUpdateReadResponse(error = readResponse.error, readResponse.organization)
    }
}
